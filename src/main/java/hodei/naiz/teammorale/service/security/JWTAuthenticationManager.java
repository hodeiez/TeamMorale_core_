package hodei.naiz.teammorale.service.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Created by Hodei Eceiza
 * Date: 1/24/2022
 * Time: 23:23
 * Project: TeamMorale
 * Copyright: MIT
 */
@Component
@AllArgsConstructor
@Qualifier
public class JWTAuthenticationManager implements ReactiveAuthenticationManager {
    private final ReactiveUserDetailsServiceImpl userDetailsService;


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        if (authentication.isAuthenticated()) {
            return Mono.just(authentication);
        }
              return Mono.just(authentication)
                .cast(UsernamePasswordAuthenticationToken.class)
                      .flatMap(a->authenticateToken(a))
                      .map(u->new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),authentication.getCredentials(), u.getAuthorities()));


    }

    private Mono<UserDetails> authenticateToken(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
        String username = usernamePasswordAuthenticationToken.getName();
        return (username != null && SecurityContextHolder.getContext().getAuthentication() == null) ? userDetailsService.findByUsername(username) : null;
    }


}
