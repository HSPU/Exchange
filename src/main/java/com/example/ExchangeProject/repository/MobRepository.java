package com.example.ExchangeProject.repository;

import com.example.ExchangeProject.entity.Mob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MobRepository extends JpaRepository<Mob, Long> {
    List<Mob> findByNameContaining(String name);
}
