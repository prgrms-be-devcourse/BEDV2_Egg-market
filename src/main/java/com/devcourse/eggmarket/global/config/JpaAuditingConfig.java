package com.devcourse.eggmarket.global.config;

import com.devcourse.eggmarket.domain.model.image.ImageLocalPathProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@EnableConfigurationProperties(value = {ImageLocalPathProperties.class})
public class JpaAuditingConfig {

}
