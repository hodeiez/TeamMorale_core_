package hodei.naiz.teammorale.service.security;

import hodei.naiz.teammorale.persistance.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * Created by Hodei Eceiza
 * Date: 1/25/2022
 * Time: 09:26
 * Project: TeamMorale
 * Copyright: MIT
 */
@Component
@AllArgsConstructor
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {
    private final UserRepo userRepo;
    @Override
    public Mono<UserDetails> findByUsername(String email) {

        return userRepo.findOneByEmail(email)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new BadCredentialsException("Not found in database")))
                .map(user->new UserAuth(user.getEmail(), user.getPassword(), List.of("ROLE_USER"))); //TODO:fix when database is implemented
    }
}
