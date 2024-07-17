package com.example.ExchangeProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KMSItemName {
    private int id;
    private String name;

    public KMSItemName(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
