package com.citse.kunduApp.security.mock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyResponse {
    private String message;
    private boolean isSuccess;
}
