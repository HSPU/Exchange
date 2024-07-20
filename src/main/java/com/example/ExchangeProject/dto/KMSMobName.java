package com.example.ExchangeProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KMSMobName {
    private Long id;
    private String name;

    public KMSMobName(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
