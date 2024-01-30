package com.citse.kunduApp.utils.models;

import lombok.Builder;
import lombok.Data;

import java.security.PrivilegedAction;

@Data
@Builder
public class UserSubListener {
    private String username;
    private String uuid;
    private String avatar;
    private RoleSpace rol;
    private boolean microphone;
    private boolean audio;
}
