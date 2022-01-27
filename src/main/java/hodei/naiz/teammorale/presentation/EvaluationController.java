package hodei.naiz.teammorale.presentation;

import hodei.naiz.teammorale.domain.Evaluation;
import hodei.naiz.teammorale.persistance.DAO.EvaluationCalculations;
import hodei.naiz.teammorale.presentation.events.EvaluationSaved;
import hodei.naiz.teammorale.presentation.events.Event;
import hodei.naiz.teammorale.presentation.events.UnauthorizedEvent;
import hodei.naiz.teammorale.presentation.mapper.resources.EvaluationResource;
import hodei.naiz.teammorale.service.EvaluationService;
import hodei.naiz.teammorale.service.security.JWTutil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 11:03
 * Project: TeamMorale
 * Copyright: MIT
 */

@RestController
@RequestMapping("evaluation")
@RequiredArgsConstructor
public class EvaluationController {
    private final EvaluationService evaluationService;
    private final JWTutil jwTutil;

    @PostMapping
    public Mono<ResponseEntity<EvaluationResource>> create(@RequestBody Evaluation evaluation) {
        return evaluationService.create(evaluation).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping
    public  Mono<ResponseEntity<EvaluationResource>> update(@RequestBody Evaluation evaluation) {
        return evaluationService.update(evaluation)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<EvaluationResource> getAll() {
        return evaluationService.getAll();
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<EvaluationResource>> deleteById(@PathVariable("id") Long id) {
        return evaluationService.delete(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping(value = "events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> listenToEvents(@RequestParam("userTeamId") String userTeamId,@RequestParam("auth")String authorization) {
       return jwTutil.validateToken(authorization.substring(7))?
       evaluationService.listenSaved(Long.parseLong(userTeamId))
                .map(EvaluationSaved::new):Flux.just(new UnauthorizedEvent("unauthorized"));
    }
    @GetMapping("/myTeamToday/{userTeamsId}")
    public Flux<EvaluationResource> getByTeamIdToday( @PathVariable("userTeamsId") Long userTeamsId) {
        return evaluationService.getByDateAndTeamId(userTeamsId);

    }
    @PostMapping("/createOrUpdate/")
    public Mono<ResponseEntity<EvaluationResource>> createOrUpdate(@RequestBody Evaluation evaluation){
        return evaluationService.createOrUpdate(evaluation).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/stats/date/{date}/team/{teamId}")
    public Mono<ResponseEntity<EvaluationCalculations>> getAverageByDateAndTeam(@PathVariable("date") String date, @PathVariable("teamId") Long teamId){
        return evaluationService.getAverageByDateAndTeam(date,teamId).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }
    @GetMapping("/stats/team/{teamId}")
    public Flux<EvaluationCalculations> getAllAverageOfDatesByTeam(@PathVariable("teamId") Long teamId){
        return evaluationService.getAllAverageOfDatesByTeam(teamId);
    }
    @GetMapping("/stats/total/team/{teamId}")
    public Mono<ResponseEntity<EvaluationCalculations>> getTotalByTeam(@PathVariable("teamId") Long teamId){
        return evaluationService.getTotalAverageByTeam(teamId).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
