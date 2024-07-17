package com.example.ExchangeProject.service;

import com.example.ExchangeProject.repository.ItemRepository;
import org.springframework.stereotype.Service;

@Service
public class DataLoaderService {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    public DataLoaderService(ItemRepository itemRepository, ItemService itemService) {
        this.itemRepository = itemRepository;
        this.itemService = itemService;
    }

    public void loadDataIfNecessary() {
        // DB에 데이터가 있는지 검사
        if (itemRepository.count() == 0) {
            // 데이터가 없다면 api 호출 및 데이터 저장
            itemService.loadDataFromApi();
        }
    }
}
