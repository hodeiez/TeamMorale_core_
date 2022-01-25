package hodei.naiz.teammorale.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Created by Hodei Eceiza
 * Date: 1/25/2022
 * Time: 23:03
 * Project: TeamMorale
 * Copyright: MIT
 */
@Configuration
@EnableWebFlux
public class CorsConfig implements WebFluxConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("PUT","POST","DELETE","GET")
                .maxAge(3600);
    }


}

