package com.citse.kunduApp.utils.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class POST {
    private String message;
    private String room;
    private String username;
    private SocketType type;
}
