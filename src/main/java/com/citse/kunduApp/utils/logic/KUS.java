package com.citse.kunduApp.utils.logic;

import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class KUS implements KunduUtilitiesService {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ012345678987654321abcdefghijklmnñopqrstuvxyz";
    private static final SecureRandom sr = new SecureRandom();

    @Override
    public String KunduCode(String acronym) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            int index = sr.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(index));
        }
        return acronym+getCurrentYear()+"-"+builder.toString();
    }

    @Override
    public Map<String, String> BrowserSpecifyHeaders(HttpServletRequest request) {
        Map<String, String> specificHeaders = new HashMap<>();
        specificHeaders.put("connection", request.getHeader("connection"));
        specificHeaders.put("browse-info", request.getHeader("sec-ch-ua"));
        specificHeaders.put("platform", request.getHeader("sec-ch-ua-platform"));
        specificHeaders.put("sys_info","/info");
        return specificHeaders;
    }

    private static String getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return Integer.toString(year).substring(2);
    }
}
