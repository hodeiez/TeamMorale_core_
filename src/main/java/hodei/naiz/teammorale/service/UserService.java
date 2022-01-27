package hodei.naiz.teammorale.service;

import hodei.naiz.teammorale.domain.User;
import hodei.naiz.teammorale.persistance.UserRepo;
import hodei.naiz.teammorale.presentation.mapper.UserMapper;
import hodei.naiz.teammorale.presentation.mapper.resources.UserAuthResource;
import hodei.naiz.teammorale.presentation.mapper.resources.UserEvaluationCalculationsResource;
import hodei.naiz.teammorale.presentation.mapper.resources.UserLoginResource;
import hodei.naiz.teammorale.presentation.mapper.resources.UserResource;
import hodei.naiz.teammorale.service.publisher.EmailServiceMessage;
import hodei.naiz.teammorale.service.publisher.EmailType;
import hodei.naiz.teammorale.service.publisher.PublisherService;
import hodei.naiz.teammorale.service.security.JWTissuer;
import hodei.naiz.teammorale.service.security.UserAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

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
    private final PublisherService publisherService;
    private final PasswordEncoder passwordEncoder;
    private final JWTissuer jWTissuer;


    @Transactional
    public Mono<UserResource> create(User user) {
        if (user.getId() != null)
            return Mono.error(new IllegalArgumentException("Id must be null"));
        return userRepo.save(user.withPassword(passwordEncoder.encode(user.getPassword()))).map(userMapper::toUserResource)
                .doOnNext(u->publisherService.sendEmail(EmailServiceMessage.buildSignedUp()
                                .username(u.getUsername())
                                .to(u.getEmail())
                                .confirmationToken("ImplementWhenSECURITY") //TODO: when security is done send token
                                .emailType(EmailType.SIGNUP)
                                .message("Message sent on "+ LocalDateTime.now().toLocalDate())
                                .build()));

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

        return userRepo.findById(id).flatMap(u -> userRepo.delete(u).then(Mono.just(u).map(userMapper::toUserResource)));


    }

    public Mono<UserAuthResource> login(UserLoginResource userlogin) {

        return userRepo.findOneByEmail(userlogin.getEmail())
                .filter(u-> passwordEncoder.matches(userlogin.getPassword(), u.getPassword()))
               .map(user->userMapper.toUserAuth(user,jWTissuer.createTokenWhenLogin(userlogin)));


    }

    public Mono<UserResource> getByEmail(String email) {
        return userRepo.findOneByEmail(email).map(userMapper::toUserResource);
    }

    public Mono<UserResource> updateMe(String authorization, User user) {

        return userRepo.findOneByEmail(authorization).flatMap(u -> {
            u.setUsername(user.getUsername());
            u.setModifiedDate(LocalDateTime.now());
            return userRepo.save(u).map(userMapper::toUserResource);
        });
    }

    public Mono<UserEvaluationCalculationsResource> getMyEvaluationCalculations(String email) {

        return userRepo.findOneByEmail(email).flatMap(u -> userRepo.getEvaluationsTeamAverageByDate(u.getId()).collectList().
                zipWith(userRepo.getEvaluationsMaxAndMin(u.getId()))
                .map(result -> new UserEvaluationCalculationsResource(result.getT1(), result.getT2())));

    }

    public Mono<UserResource> changePass(String authorization, UserLoginResource creds) {

        return userRepo.findOneByEmail(authorization).flatMap(u -> {
            if (passwordEncoder.matches(creds.getOldPassword(),u.getPassword())) {
                u.setPassword(passwordEncoder.encode(creds.getPassword()));
                return userRepo.save(u).map(userMapper::toUserResource);
            }
            return Mono.error(new IllegalArgumentException("Old password didn't match"));
        });
    }
    public Mono<String> forgotPass(String email){
      return  userRepo.findOneByEmail(email).switchIfEmpty(Mono.error(new IllegalArgumentException("user not found")))
              .doOnSuccess(u->publisherService.sendEmail(EmailServiceMessage.buildForgotPass()
                      .emailType(EmailType.FORGOT_PASS)
                      .username(u.getUsername())
                      .message("follow the instructions")
                      .confirmationToken("to implement")
                      .to(email)
                      .build()))
              .map(u->"Instructions sent to " +u.getEmail());

    }
    public Mono<UserResource> resetPass(String resetPassToken,UserLoginResource userLoginResource){
        //TODO: implement resetPassToken validation when security on
        return resetPassToken.equals("to implement")?userRepo.updatePasswordByEmail(userLoginResource.getEmail(), userLoginResource.getPassword())
                .map(userMapper::toUserResource):Mono.error(new IllegalArgumentException("error"));
    }
}
