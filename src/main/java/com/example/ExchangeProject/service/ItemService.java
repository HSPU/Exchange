package com.example.ExchangeProject.service;

import com.example.ExchangeProject.entity.Item;
import com.example.ExchangeProject.dto.KMSItemName;
import com.example.ExchangeProject.repository.ItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final RestTemplate restTemplate;

    private static final String GMS_API_URL = "https://maplestory.io/api/GMS/62/item/list";
    private static final String KMS_API_URL = "https://maplestory.io/api/KMS/389/item/%d/name";

    public ItemService(ItemRepository itemRepository, RestTemplate restTemplate) {
        this.itemRepository = itemRepository;
        this.restTemplate = restTemplate;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public List<Item> searchItems(String name) {
        if (name != null) {
            return itemRepository.findByNameContaining(name);
        } else {
            return null;
        }
    }

    public void loadDataFromApi() {
        // GMS 데이터 호출 및 저장
        ResponseEntity<Item[]> response = restTemplate.getForEntity(GMS_API_URL, Item[].class);
        Item[] items = response.getBody();
        if (items != null) {
            for (Item item : items) {
                itemRepository.save(item);
            }
        }
        CompletableFuture<Void> updateFuture = updateItemNames();
        updateFuture.join();
    }

    @Async
    public CompletableFuture<Void> updateItemNames() {
        List<Item> items = itemRepository.findAll();
        List<CompletableFuture<Void>> futures = items.stream()
                .map(item -> CompletableFuture.runAsync(() -> updateItemName(item), Executors.newFixedThreadPool(10)))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private void updateItemName(Item item) {
        String kmsApiUrl = String.format(KMS_API_URL, item.getId());
        try {
            ResponseEntity<KMSItemName> response = restTemplate.getForEntity(kmsApiUrl, KMSItemName.class);
            KMSItemName kmsItemName = response.getBody();
            if (kmsItemName != null) {
                item.setName(kmsItemName.getName());
                itemRepository.save(item);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            retryUpdateItemName(item, 3);
        }
    }

    private void retryUpdateItemName(Item item, int retryCount) {
        String kmsApiUrl = String.format(KMS_API_URL, item.getId());
        for (int i = 0; i < retryCount; i++) {
            try {
                ResponseEntity<KMSItemName> response = restTemplate.getForEntity(kmsApiUrl, KMSItemName.class);
                KMSItemName kmsItemName = response.getBody();
                if (kmsItemName != null) {
                    item.setName(kmsItemName.getName());
                    itemRepository.save(item);
                    return;
                }
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                System.err.println("재시도 " + (i + 1) + " 실패한 아이템 : " + item.getId());
            }
        }
    }
}
