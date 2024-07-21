package com.example.ExchangeProject.controller;

import com.example.ExchangeProject.entity.Mob;
import com.example.ExchangeProject.service.MobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mobs")
public class MobController {
    private final MobService mobService;

    public MobController(MobService mobService) {
        this.mobService = mobService;
    }

    @GetMapping
    public List<Mob> getAllMobs() {
        return mobService.getAllMobs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mob> getMobById(@PathVariable("id") Long id) {
        Optional<Mob> mob = mobService.getMobById(id);
        return mob.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<Mob> searchMobs(@RequestParam(value = "name", required = false) String name) {
        return mobService.searchMobs(name);
    }
}
