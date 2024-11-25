package com.systemedebons.bonification.payload.dto;

import lombok.Data;

@Data
public class RewardDTO {
    private String nom;

    private String description;

    private int points;

    private String userId;
}
