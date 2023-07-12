package com.citse.kunduApp.utils.contracts;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface KunduUtilitiesService {
    String KunduCode(String acronym);
    Map<String,String> BrowserSpecifyHeaders(HttpServletRequest request);
}
