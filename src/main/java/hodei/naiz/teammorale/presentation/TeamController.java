package hodei.naiz.teammorale.presentation;

import hodei.naiz.teammorale.domain.Team;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamAndMembersResource;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamResource;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamUpdateResource;
import hodei.naiz.teammorale.service.TeamService;
import hodei.naiz.teammorale.service.security.JWTissuer;
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
    private final JWTissuer jwTissuer;

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
    @GetMapping("/myTeams/")
    public Flux<TeamAndMembersResource> getMyTeams(@RequestHeader(value="Authorization") String authorization){
        return teamService.getByEmail(jwTissuer.getUserEmail(authorization));
    }
    @GetMapping("/id/{teamId}")
    public Mono<ResponseEntity<TeamAndMembersResource>> getOneTeam (@PathVariable("teamId") Long teamId){
        return teamService.getOne(teamId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("/addUser/user/{userId}/team/{teamId}")
    public Mono<ResponseEntity<Long>> addUserToTeam (@PathVariable("userId") Long userId,@PathVariable("teamId") Long teamId){
        return teamService.addUserToTeam(userId,teamId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("/addemails")
    public Mono<ResponseEntity<TeamAndMembersResource>> addUserEmailsToTeam(@RequestBody TeamAndMembersResource team){
        return teamService.addUsersToTeam(team.getMembers(), team.getId()).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("/createTeamWithEmails/")
    public Mono<ResponseEntity<TeamAndMembersResource>> createTeamWithEmails(@RequestBody TeamAndMembersResource team,@RequestHeader(value="Authorization") String authorization){
        return teamService.createWithUsers(team,jwTissuer.getUserEmail(authorization)).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/update")
    public  Mono<ResponseEntity<TeamAndMembersResource>> updateTeam(@RequestHeader(value="Authorization") String authorization,@RequestBody TeamUpdateResource teamUpdateResource) {
        return teamService.updateTeam(jwTissuer.getUserEmail(authorization),teamUpdateResource).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/unsubscribeMe/from/{teamId}")
    public Mono<ResponseEntity<TeamResource>> unsubscribeMe(@RequestHeader(value="Authorization") String authorization ,@PathVariable("teamId") Long teamId){

        return teamService.unsubscribeUserByEmail(jwTissuer.getUserEmail(authorization),teamId).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/delete/full/userTeam/{userTeamId}")
    public Mono<ResponseEntity<TeamResource>> deleteFull(@RequestHeader(value="Authorization") String authorization ,@PathVariable("userTeamId") Long userTeamId){

        return teamService.deleteTeamFull(jwTissuer.getUserEmail(authorization),userTeamId).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
