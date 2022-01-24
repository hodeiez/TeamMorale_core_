package hodei.naiz.teammorale.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Created by Hodei Eceiza
 * Date: 1/24/2022
 * Time: 22:46
 * Project: TeamMorale
 * Copyright: MIT
 */
@Configuration
@EnableReactiveMethodSecurity //TODO: uncomment to use security
@EnableWebFluxSecurity
public class SecurityConfiguration {
    @Value("${security.key}")
    private String key;

    @Value("${security.duration}")
    private Integer duration;

    @Value("${security.algorithm}")
    private String algorithm;

    @Bean
    public SecurityWebFilterChain chain(ServerHttpSecurity http){
        http
                // disable CSRF
                .csrf().disable()

                // add AuthenticationWebFilter and set the handler
                .formLogin()
                //.authenticationSuccessHandler(new WebFilterChainServerAuthenticationSuccessHandler())
                //.authenticationFailureHandler(((webFilterExchange, exception) -> Mono.error(exception)))

                // allow all path accessed by all role
                .and()
                .authorizeExchange()
                .pathMatchers("/**").permitAll()

                .and()
               // .addFilterAt(jwtAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                .httpBasic();

        return http.build();
    }
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();

    }
}
