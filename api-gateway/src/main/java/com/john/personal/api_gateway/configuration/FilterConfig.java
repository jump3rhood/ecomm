package com.john.personal.api_gateway.configuration;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@Configuration
public class FilterConfig {

    @Value("${gateway.excludeUrls}")
    private String excludeUrls;

    @Bean
    @Qualifier("excludeUrls")
    public List<String> excludeUrls(){
        return Arrays.stream(excludeUrls.split(",")).toList();
    }

    @Bean
    public ObjectMapper objectMapper(){
        JsonFactory factory = new JsonFactory();
        // ignore unknown
        factory.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss"));
        return mapper;
    }
}
