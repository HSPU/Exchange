package com.example.ExchangeProject.service;

import com.example.ExchangeProject.dto.KMSMobName;
import com.example.ExchangeProject.entity.Mob;
import com.example.ExchangeProject.repository.MobRepository;
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
public class MobService {
    private final MobRepository mobRepository;
    private final RestTemplate restTemplate;

    private static final String GMS_API_URL = "https://maplestory.io/api/GMS/62/mob";
    private static final String KMS_API_URL = "https://maplestory.io/api/KMS/389/mob/%d/name";

    public MobService(MobRepository mobRepository, RestTemplate restTemplate) {
        this.mobRepository = mobRepository;
        this.restTemplate = restTemplate;
    }

    public List<Mob> getAllMobs() {
        return mobRepository.findAll();
    }

    public Optional<Mob> getMobById(Long id) {
        return mobRepository.findById(id);
    }

    public List<Mob> searchMobs(String name) {
        if (name != null) {
            return mobRepository.findByNameContaining(name);
        } else {
            return null;
        }
    }

    public void loadDataFromApi() {
        // GMS 데이터 호출 및 저장
        ResponseEntity<Mob[]> response = restTemplate.getForEntity(GMS_API_URL, Mob[].class);
        Mob[] mobs = response.getBody();
        if (mobs != null) {
            for (Mob mob : mobs) {
                mobRepository.save(mob);
            }
        }
        CompletableFuture<Void> updateFuture = updateMobNames();
        updateFuture.join();
    }

    @Async
    public CompletableFuture<Void> updateMobNames() {
        List<Mob> mobs = mobRepository.findAll();
        List<CompletableFuture<Void>> futures = mobs.stream()
                .map(mob -> CompletableFuture.runAsync(() -> updateMobName(mob), Executors.newFixedThreadPool(5)))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private void updateMobName(Mob mob) {
        String kmsApiUrl = String.format(KMS_API_URL, mob.getId());
        try {
            ResponseEntity<KMSMobName> response = restTemplate.getForEntity(kmsApiUrl, KMSMobName.class);
            KMSMobName kmsMobName = response.getBody();
            if (kmsMobName != null) {
                mob.setName(kmsMobName.getName());
                mobRepository.save(mob);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            retryUpdateMobName(mob, 3);
        }
    }

    private void retryUpdateMobName(Mob mob, int retryCount) {
        String kmsApiUrl = String.format(KMS_API_URL, mob.getId());
        for (int i = 0; i < retryCount; i++) {
            try {
                ResponseEntity<KMSMobName> response = restTemplate.getForEntity(kmsApiUrl, KMSMobName.class);
                KMSMobName kmsMobName = response.getBody();
                if (kmsMobName != null) {
                    mob.setName(kmsMobName.getName());
                    mobRepository.save(mob);
                    return;
                }
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                System.err.println("재시도 " + (i + 1) + " 실패한 몬스터 : " + mob.getId());
            }
        }
    }
}
