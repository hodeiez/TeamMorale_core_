package hodei.naiz.teammorale.presentation;

import hodei.naiz.teammorale.presentation.mapper.resources.TeamAndMembersResource;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamResource;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamUpdateResource;
import hodei.naiz.teammorale.service.TeamService;
import hodei.naiz.teammorale.service.security.JWTutil;
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
    private final JWTutil jwTutil;


    @GetMapping("/myTeams/")
    public Flux<TeamAndMembersResource> getMyTeams(@RequestHeader(value="Authorization") String authorization){
        return teamService.getByEmail(jwTutil.getUserEmail(authorization));
    }
    @GetMapping("/id/{teamId}")
    public Mono<ResponseEntity<TeamAndMembersResource>> getOneTeam (@PathVariable("teamId") Long teamId){
        return teamService.getOne(teamId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/createTeamWithEmails/")
    public Mono<ResponseEntity<TeamAndMembersResource>> createTeamWithEmails(@RequestBody TeamAndMembersResource team,@RequestHeader(value="Authorization") String authorization){
        return teamService.createWithUsers(team, jwTutil.getUserEmail(authorization)).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/update")
    public  Mono<ResponseEntity<TeamAndMembersResource>> updateTeam(@RequestHeader(value="Authorization") String authorization,@RequestBody TeamUpdateResource teamUpdateResource) {
        return teamService.updateTeam(jwTutil.getUserEmail(authorization),teamUpdateResource).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/unsubscribeMe/from/{teamId}")
    public Mono<ResponseEntity<TeamResource>> unsubscribeMe(@RequestHeader(value="Authorization") String authorization ,@PathVariable("teamId") Long teamId){

        return teamService.unsubscribeUserByEmail(jwTutil.getUserEmail(authorization),teamId).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/delete/full/userTeam/{userTeamId}")
    public Mono<ResponseEntity<TeamResource>> deleteFull(@RequestHeader(value="Authorization") String authorization ,@PathVariable("userTeamId") Long userTeamId){

        return teamService.deleteTeamFull(jwTutil.getUserEmail(authorization),userTeamId).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
