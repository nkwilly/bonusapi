package com.systemedebons.bonification.payload.response;



import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String email;
    private String refreshToken;
    private List<String> roles;

    public JwtResponse(String accessToken,String refreshToken, String id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.refreshToken = refreshToken;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}





