package hodei.naiz.teammorale.presentation;

import hodei.naiz.teammorale.domain.Team;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamResource;
import hodei.naiz.teammorale.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Hodei Eceiza
 * Date: 1/3/2022
 * Time: 14:36
 * Project: TeamMorale
 * Copyright: MIT
 */
@RestController
@RequestMapping("team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public Mono<TeamResource> create(@RequestBody Team team) {
        return teamService.create(team);
    }

    @PutMapping
    public  Mono<ResponseEntity<TeamResource>> update(@RequestBody Team team) {
        return teamService.update(team)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<TeamResource> getAll() {
        return teamService.getAll();
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<TeamResource>> deleteById(@PathVariable("id") Long id) {
        return teamService.delete(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    /*extra endpoints*/
    @PostMapping("/addUser/user/{userId}/team/{teamId}")
    public Mono<ResponseEntity<Long>> addUserToTeam (@PathVariable("userId") Long userId,@PathVariable("teamId") Long teamId){
        return teamService.addUserToTeam(userId,teamId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
