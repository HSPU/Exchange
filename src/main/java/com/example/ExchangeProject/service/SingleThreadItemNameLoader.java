//package com.example.ExchangeProject.service;
//
//import com.example.ExchangeProject.entity.Item;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//@Component
//public class SingleThreadItemNameLoader implements CommandLineRunner {
//
//    @Autowired
//    private ItemService itemService;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    @Override
//    public void run(String... args) throws Exception {
//        long startTime = System.currentTimeMillis();
//
//        List<Item> items = itemService.getAllItems();
//        for (Item item : items) {
//            updateItemName(item);
//        }
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("Single-threaded processing time: " + (endTime - startTime) + "ms");
//    }
//
//    private void updateItemName(Item item) {
//        String kmsApiUrl = "https://maplestory.io/api/kms/389/item/" + item.getId() + "/name";
//        try {
//            String jsonResponse = restTemplate.getForObject(kmsApiUrl, String.class);
//            if (jsonResponse != null) {
//                String koreanName = new ObjectMapper().readTree(jsonResponse).get("name").asText();
//                item.setName(koreanName);
//                itemService.saveItem(item);
//            }
//        } catch (Exception e) {
//            System.err.println("Error updating item name: " + e.getMessage());
//        }
//    }
//}
