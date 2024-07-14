package com.example.ExchangeProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Item {
    @Id
    private Long id;

    @ElementCollection
    @CollectionTable(name = "item_required_jobs", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "job")
    private List<String> requiredJobs;

    private int requiredLevel;
    private boolean isCash;
    private int requiredGender;
    private String name;
    private String description;

    private TypeInfo typeInfo;
}
