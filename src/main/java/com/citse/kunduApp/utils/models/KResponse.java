package com.citse.kunduApp.utils.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KResponse {
    private String url;
    private String origin;
    private Object body;
    private String status;

}
