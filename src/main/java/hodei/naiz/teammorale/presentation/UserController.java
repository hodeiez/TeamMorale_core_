package hodei.naiz.teammorale.presentation;

import hodei.naiz.teammorale.domain.User;
import hodei.naiz.teammorale.presentation.mapper.resources.UserEvaluationCalculationsResource;
import hodei.naiz.teammorale.presentation.mapper.resources.UserLoginResource;
import hodei.naiz.teammorale.presentation.mapper.resources.UserResource;
import hodei.naiz.teammorale.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:31
 * Project: UserMorale
 * Copyright: MIT
 */
@CrossOrigin
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public Mono<UserResource> create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public  Mono<ResponseEntity<UserResource>> update(@RequestBody User user) {
        return userService.update(user)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<UserResource> getAll() {
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<UserResource>> deleteById(@PathVariable("id") Long id) {
        return userService.delete(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    /*
    extra endpoints
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<UserResource>> login(@RequestBody UserLoginResource userlogin){
        return userService.login(userlogin).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/getMe")
    public Mono<ResponseEntity<UserResource>> getMe(@RequestHeader(value="Authorization") String authorization){
        return userService.getByEmail(authorization).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/updateMe")
    public Mono<ResponseEntity<UserResource>> updateMe(@RequestHeader(value="Authorization") String authorization, @RequestBody User user){
        return userService.updateMe(authorization,user).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/getMyStats")
    public Mono<ResponseEntity<UserEvaluationCalculationsResource>> getMyStats(@RequestHeader(value="Authorization") String authorization){
        return userService.getByEmail(authorization).flatMap(u->userService.getMyEvaluationCalculations(authorization)).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("/changePass")
    public Mono<ResponseEntity<UserResource>> updateMe(@RequestHeader(value="Authorization") String authorization, @RequestBody UserLoginResource user){
        return userService.changePass(authorization,user).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("/forgotPass/email/{email}")
    public Mono<ResponseEntity<String>> forgotPass(@PathVariable("email") String email){
        return userService.forgotPass(email).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("/resetPass")
    public Mono<ResponseEntity<UserResource>> resetPass(@RequestHeader(value="Authorization") String passResetToken,@RequestBody UserLoginResource userLoginResource){
        return userService.resetPass(passResetToken,userLoginResource).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
