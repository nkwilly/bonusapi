package com.systemedebons.bonification.payload.request;

import lombok.Data;

@Data
public class HistoryRequest {
    private String id;

    private int points;

    private String transactionId;
}
