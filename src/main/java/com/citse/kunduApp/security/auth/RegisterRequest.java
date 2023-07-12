package com.citse.kunduApp.security.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    // USER ATTRIBUTES
    private String username;
    private String password;
    private String email;
    private Integer secure;
    // PERSON ATTRIBUTES
    private String name;
    private String phone;
    private String avatar;
}
