//package com.example.ExchangeProject.service;
//
//import com.example.ExchangeProject.entity.Item;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@Component
//public class KMSItemNameLoader implements CommandLineRunner {
//
//    private final ItemService itemService;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public KMSItemNameLoader(ItemService itemService) {
//        this.itemService = itemService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        long startTime = System.currentTimeMillis();
//
//        // DB 에서 모든 아이템 불러오기
//        List<Item> items = itemService.getAllItems();
//
//        // threadPool 생성
//        ExecutorService executor = Executors.newFixedThreadPool(10);
//
//        // CompletableFuture 리스트 생성
//        List<CompletableFuture<Void>> futures = items.stream()
//                .map(item -> CompletableFuture.runAsync(() -> updateItemName(item), executor))
//                .toList();
//
//        // 모든 CompletableFuture 완료 대기
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
//
//        // threadPool 종료
//        executor.shutdown();
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("Parallel processing time: " + (endTime - startTime) + "ms");
//    }
//
//    private void updateItemName(Item item) {
//        String kmsApiUrl = "https://maplestory.io/api/kms/389/item/" + item.getId() + "/name";
////        System.out.println("Requesting URL: " + kmsApiUrl);
//
//        boolean success = false;
//        int attempt = 0;
//        int maxAttempts = 5;
//
//        while (!success && attempt < maxAttempts) {
//            attempt++;
//            try {
//                String jsonResponse = restTemplate.getForObject(kmsApiUrl, String.class);
//                if (jsonResponse != null) {
//                    // JSON 파싱해서 name 필드 값만 추출하기
//                    JsonNode rootNode = objectMapper.readTree(jsonResponse);
//                    JsonNode nameNode = rootNode.get("name");
//                    if (nameNode != null) {
//                        String koreanName = nameNode.asText();
//                        item.setName(koreanName);
//                        itemService.saveItem(item);
//                        success = true;
//                    } else {
//                        System.err.println("해당 Item ID 의 name 필드를 찾을 수 없습니다.: " + item.getId());
//                    }
//                }
//            } catch (HttpClientErrorException | HttpServerErrorException e) {
//                System.err.println("해당 Item ID 의 이름을 가져오지 못했습니다.: " + item.getId() + " - " + e.getStatusCode());
//                // api 호출 실패 처리
//                if (e.getStatusCode().is5xxServerError()) {
//                    try {
//                        Thread.sleep(1000 * attempt);
//                    } catch (InterruptedException interruptedException) {
//                        Thread.currentThread().interrupt();
//                    }
//                }
//            } catch (Exception e) {
//                System.err.println("Item ID 에서 예상치 못한 에러 발생: " + item.getId() + " - " + e.getMessage());
//            }
//        }
//
//        if (!success) {
//            System.err.println("Item ID 의 name 업데이트 실패: " + item.getId() + " after " + maxAttempts + " attempts");
//        }
//    }
//}
