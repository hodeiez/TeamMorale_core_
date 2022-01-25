package hodei.naiz.teammorale.config;

import hodei.naiz.teammorale.service.security.JWTTokenAuthenticationFilter;
import hodei.naiz.teammorale.service.security.JWTissuer;
import lombok.AllArgsConstructor;
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

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                JWTissuer tokenProvider,
                                                ReactiveAuthenticationManager reactiveAuthenticationManager) {


        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(it -> it
                        .pathMatchers("/team/**").authenticated()
                        .pathMatchers("/user/login").permitAll()
                        .pathMatchers("/user").permitAll()

                        .anyExchange().permitAll()
                )
                .addFilterAt(new JWTTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();


    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();

    }

    }


