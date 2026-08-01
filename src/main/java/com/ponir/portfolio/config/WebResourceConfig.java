package com.ponir.portfolio.config;

import com.ponir.portfolio.service.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class WebResourceConfig implements WebMvcConfigurer {
    private final ImageStorageService imageStorageService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(imageStorageService.resourceLocation())
                .setCachePeriod((int) Duration.ofHours(1).toSeconds());
    }
}
