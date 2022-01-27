package hodei.naiz.teammorale.config;

import hodei.naiz.teammorale.service.security.JWTTokenAuthenticationFilter;
import hodei.naiz.teammorale.service.security.JWTutil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Created by Hodei Eceiza
 * Date: 1/24/2022
 * Time: 22:46
 * Project: TeamMorale
 * Copyright: MIT
 */
@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
@AllArgsConstructor
public class SecurityConfiguration {
   private WebAppProperties webAppProperties;
    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                JWTutil tokenProvider,
                                                ReactiveAuthenticationManager reactiveAuthenticationManager) {


        return http.csrf(ServerHttpSecurity.CsrfSpec::disable).cors().configurationSource( corsConfigurationSource() ).and()
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(it -> it
                        .pathMatchers("/user/login").permitAll()
                        .pathMatchers("/user/signup").permitAll()
                        .pathMatchers("/evaluation/events").permitAll()
                        .pathMatchers("/user/forgotPass/**").permitAll()
                        .pathMatchers("/user/resetPass/**").permitAll()
                        .pathMatchers("/user/verifyMe").permitAll()

                        .pathMatchers("/team/**").authenticated()
                        .pathMatchers("/evaluation/**").authenticated()
                        .pathMatchers("/user/**").authenticated()


                        .anyExchange().permitAll()
                )
                .addFilterAt(new JWTTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();


    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();

    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(webAppProperties.getClientUrl()));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        configuration.setMaxAge(3600L);
        configuration.applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    }


