package com.example.ExchangeProject.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class TypeInfo {
    private String overallCategory;
    private String category;
    private String subCategory;
    private Long lowItemId;
    private Long highItemId;
}