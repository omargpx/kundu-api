package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.utils.models.KResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.util.Map;

public interface KunduUtilitiesService {
    String KunduCode(String acronym);
    Map<String,String> BrowserSpecifyHeaders(HttpServletRequest request);
    KResponse getResponse(HttpServletRequest url, String origin,Object data, HttpStatus status);
    String SecureCode(String acronym);
}
