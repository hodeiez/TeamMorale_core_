package hodei.naiz.teammorale.service;

import hodei.naiz.teammorale.domain.User;
import hodei.naiz.teammorale.persistance.TeamRepo;
import hodei.naiz.teammorale.persistance.UserRepo;
import hodei.naiz.teammorale.presentation.mapper.UserMapper;
import hodei.naiz.teammorale.presentation.mapper.resources.UserEvaluationCalculationsResource;
import hodei.naiz.teammorale.presentation.mapper.resources.UserLoginResource;
import hodei.naiz.teammorale.presentation.mapper.resources.UserResource;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final TeamRepo teamRepo;

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
    public Mono<UserResource> login(UserLoginResource userlogin){
        return userRepo.findOneByEmailAndPassword(userlogin.getEmail(), userlogin.getPassword()).map(userMapper::toUserResource);
    }
    public Mono<UserResource> getByEmail(String email){
        return userRepo.findOneByEmail(email).map(userMapper::toUserResource);
    }

    public Mono<UserResource> updateMe(String authorization, User user) {

        return userRepo.findOneByEmail(authorization).flatMap(u -> {
            u.setUsername(user.getUsername());
            u.setModifiedDate(LocalDateTime.now());
            return userRepo.save(u).map(userMapper::toUserResource);
        });
    }
    public Mono<UserEvaluationCalculationsResource> getMyEvaluationCalculations(String email){

            return userRepo.findOneByEmail(email).flatMap(u->userRepo.getEvaluationsTeamAverageByDate(u.getId()).collectList().
                    zipWith(userRepo.getEvaluationsMaxAndMin(u.getId()))
                    .map(result->new UserEvaluationCalculationsResource(result.getT1(),result.getT2())));

    }
    public Mono<UserResource> changePass(String authorization, UserLoginResource creds){
        return userRepo.findOneByEmail(authorization).flatMap(u->{if(u.getPassword().equals(creds.getOldPassword())){
        u.setPassword(creds.getPassword()); return userRepo.save(u).map(userMapper::toUserResource);}return  Mono.error(new IllegalArgumentException("Old password didn't match")) ;
        });
    }
}
