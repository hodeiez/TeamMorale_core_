package hodei.naiz.teammorale.presentation;

import hodei.naiz.teammorale.domain.User;
import hodei.naiz.teammorale.presentation.mapper.resources.UserAuthResource;
import hodei.naiz.teammorale.presentation.mapper.resources.UserEvaluationCalculationsResource;
import hodei.naiz.teammorale.presentation.mapper.resources.UserLoginResource;
import hodei.naiz.teammorale.presentation.mapper.resources.UserResource;
import hodei.naiz.teammorale.service.UserService;
import hodei.naiz.teammorale.service.security.JWTutil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:31
 * Project: UserMorale
 * Copyright: MIT
 */

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JWTutil jwTutil;


    @PostMapping("/signup")
    public Mono<ResponseEntity<UserResource>> signUp(@RequestBody User user) {
        return userService.create(user).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("/login")
    public Mono<ResponseEntity<UserAuthResource>> login(@RequestBody UserLoginResource userlogin){
        return userService.login(userlogin).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/getMe")
    public Mono<ResponseEntity<UserResource>> getMe(@RequestHeader(value="Authorization") String authorization){
        return userService.getByEmail(jwTutil.getUserEmail(authorization)).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/updateMe")
    public Mono<ResponseEntity<UserResource>> updateMe(@RequestHeader(value="Authorization") String authorization, @RequestBody User user){
        return userService.updateMe(jwTutil.getUserEmail(authorization),user).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/getMyStats")
    public Mono<ResponseEntity<UserEvaluationCalculationsResource>> getMyStats(@RequestHeader(value="Authorization") String authorization){
        return userService.getByEmail(jwTutil.getUserEmail(authorization))
                .flatMap(u->userService.getMyEvaluationCalculations(jwTutil.getUserEmail(authorization))).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("/changePass")
    public Mono<ResponseEntity<UserResource>> updateMyPass(@RequestHeader(value="Authorization") String authorization, @RequestBody UserLoginResource user){
        return userService.changePass(jwTutil.getUserEmail(authorization),user).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("forgotPass/email/{email}")
    public Mono<ResponseEntity<String>> forgotPass(@PathVariable("email") String email){
        return userService.forgotPass(email).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("/resetPass")
    public Mono<ResponseEntity<UserResource>> resetPass(@RequestHeader(value="Authorization") String passResetToken,@RequestBody UserLoginResource userLoginResource){
        return userService.resetPass(passResetToken,userLoginResource).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/verifyMe")
    public Mono<ResponseEntity<UserResource>> verifyUser(@RequestHeader(value="Authorization") String authorization){
        return userService.verifyAccount(authorization).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/deleteMe")
    public Mono<String> deleteME(@RequestHeader(value="authorization") String authorization){
        return userService.deleteMe(authorization).then(Mono.just("User deleted"));
    }

}
