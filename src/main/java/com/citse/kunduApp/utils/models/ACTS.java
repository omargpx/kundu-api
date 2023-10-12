package com.citse.kunduApp.utils.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ACTS {
    private String room;
    private Object data;
    private SocketType type;
}
