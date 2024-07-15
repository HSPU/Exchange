package com.example.ExchangeProject.service;

import com.example.ExchangeProject.entity.Item;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class KMSItemNameLoader implements CommandLineRunner {

    private final ItemService itemService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KMSItemNameLoader(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // DB 에서 모든 아이템 불러오기
        List<Item> items = itemService.getAllItems();

        for (Item item : items) {
            String kmsApiUrl = "https://maplestory.io/api/kms/389/item/" + item.getId() + "/name";

            try {
                String jsonResponse = restTemplate.getForObject(kmsApiUrl, String.class);
                if (jsonResponse != null) {
                    // JSON 파싱해서 name 필드 값만 추출하기
                    JsonNode rootNode = objectMapper.readTree(jsonResponse);
                    JsonNode nameNode = rootNode.get("name");
                    if (nameNode != null) {
                        String koreanName = nameNode.asText();
                        item.setName(koreanName);
                        itemService.saveItem(item);
                    } else {
                        System.err.println("해당 Item ID 의 name 필드를 찾을 수 없습니다.: " + item.getId());
                    }
                }
            } catch (HttpClientErrorException e) {
                // api 호출 실패 처리
                if (e.getStatusCode().value() == 404) {
                    System.err.println("KMS 에서 Item ID를 찾을 수 없습니다.: " + item.getId());
                } else {
                    System.err.println("해당 Item ID 의 이름을 가져오지 못했습니다.: " + item.getId() + " - " + e.getStatusCode());
                }
            }
        }
    }
}
