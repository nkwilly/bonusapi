package com.systemedebons.bonification.payload.request;

import lombok.Data;

@Data
public class TokenRefreshRequest {

    private String refreshToken;
}
