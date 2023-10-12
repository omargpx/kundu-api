package com.citse.kunduApp.utils.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSubListener {
    private String username;
    private String uuid;
    private String avatar;
}
