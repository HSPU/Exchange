package com.example.ExchangeProject.service;

import com.example.ExchangeProject.repository.ItemRepository;
import com.example.ExchangeProject.repository.MobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataLoaderService {

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final MobRepository mobRepository;
    private final MobService mobService;

    public void loadDataIfNecessary() {
        // DB에 데이터가 있는지 검사
        if (itemRepository.count() == 0) {
            // 데이터가 없다면 api 호출 및 데이터 저장
            itemService.loadDataFromApi();
        }

        if (mobRepository.count() == 0) {
            mobService.loadDataFromApi();
        }
    }
}
