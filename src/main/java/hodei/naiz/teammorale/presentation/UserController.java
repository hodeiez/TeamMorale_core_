package hodei.naiz.teammorale.presentation;

import hodei.naiz.teammorale.domain.User;
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
}
