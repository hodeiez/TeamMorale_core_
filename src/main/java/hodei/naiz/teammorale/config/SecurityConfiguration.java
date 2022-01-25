package hodei.naiz.teammorale.config;

import hodei.naiz.teammorale.persistance.UserRepo;
import hodei.naiz.teammorale.service.security.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.netty.handler.codec.base64.Base64;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.List;

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
                        .pathMatchers("/team").authenticated()
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


