package com.example.ExchangeProject.service;

import com.example.ExchangeProject.entity.Item;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataLoader implements CommandLineRunner {
    private final ItemService itemService;

    public DataLoader(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://maplestory.io/api/GMS/62/item/list";

        Item[] items = restTemplate.getForObject(apiUrl, Item[].class);

        if (items != null) {
            for (Item item :items) {
                itemService.saveItem(item);
            }
        }
    }
}
