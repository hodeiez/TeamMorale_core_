package hodei.naiz.teammorale.service;

import hodei.naiz.teammorale.domain.User;
import hodei.naiz.teammorale.persistance.UserRepo;
import hodei.naiz.teammorale.presentation.mapper.UserMapper;
import hodei.naiz.teammorale.presentation.mapper.resources.UserResource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:17
 * Project: TeamMorale
 * Copyright: MIT
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Transactional
    public Mono<UserResource> create(User user) {
        if (user.getId() != null)
            return Mono.error(new IllegalArgumentException("Id must be null"));
        return userRepo.save(user).map(userMapper::toUserResource);
    }

    @Transactional
    public Mono<UserResource> update(User user) {
        if (user.getId() != null) {
            return userRepo.findById(user.getId())
                    .flatMap(u -> {
                        u.setUsername(user.getUsername());
                        u.setModifiedDate(LocalDateTime.now());
                        return userRepo.save(u).map(userMapper::toUserResource);
                    });

        }
        return Mono.error(new IllegalArgumentException("Need an Id to update a user"));

    }

    public Flux<UserResource> getAll() {
        return userRepo.findAll().map(userMapper::toUserResource);
    }

    @Transactional
    public Mono<UserResource> delete(Long id) {

        return userRepo.findById(id).flatMap(u->userRepo.delete(u).then(Mono.just(u).map(userMapper::toUserResource)));


    }
}