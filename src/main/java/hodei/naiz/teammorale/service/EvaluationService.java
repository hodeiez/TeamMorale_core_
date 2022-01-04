package hodei.naiz.teammorale.service;

import hodei.naiz.teammorale.domain.Evaluation;
import hodei.naiz.teammorale.domain.Team;
import hodei.naiz.teammorale.domain.User;
import hodei.naiz.teammorale.persistance.EvaluationRepo;
import hodei.naiz.teammorale.persistance.TeamRepo;
import hodei.naiz.teammorale.persistance.UserRepo;
import hodei.naiz.teammorale.presentation.mapper.EvaluationMapper;
import hodei.naiz.teammorale.presentation.mapper.resources.EvaluationResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:55
 * Project: TeamMorale
 * Copyright: MIT
 */
@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepo evaluationRepo;
    private final TeamRepo teamRepo;
    private final UserRepo userRepo;
    private final EvaluationMapper evaluationMapper;

    /**
     * Creates an evaluation, if there is no an evaluation with same userTeamId and date.
     * @param evaluation
     * @return evaluation mapped (id,user name,team name, energy,production,well_being, and date)
     */
    @Transactional
    public Mono<EvaluationResource> create(Evaluation evaluation) {
        if (evaluation.getId() != null)
            return Mono.error(new IllegalArgumentException("Id must be null"));
        return evaluationRepo.evaluationExists(evaluation.getUserTeamsId(), todayDateString())
                .flatMap(exists -> !exists ? Mono.just(evaluation)
                        .zipWith(userRepo.getByUserTeamsId(evaluation.getUserTeamsId()).map(User::getId))
                        .map(result -> result.getT1().setUserId(result.getT2()))
                        .zipWith(teamRepo.getByUserTeamsId(evaluation.getUserTeamsId()).map(Team::getId))
                        .map(result -> result.getT1().setTeamId(result.getT2()))
                        .flatMap(evaluation1 ->evaluationRepo.save(evaluation1)
                                .flatMap(this::getRelations).map(evaluationMapper::toEvaluationResource))
                        : Mono.error(new IllegalArgumentException("There is an evaluation registered this date")));
    }

    @Transactional
    public Mono<EvaluationResource> update(Evaluation evaluation) {
        if (evaluation.getId() != null) {
            return evaluationRepo.findById(evaluation.getId())
                    .flatMap(e -> {
                        e.setEnergy(evaluation.getEnergy());
                        e.setWellBeing(evaluation.getWellBeing());
                        e.setProduction(evaluation.getProduction());
                        e.setModifiedDate(LocalDateTime.now());
                        return evaluationRepo.save(e).flatMap(this::getRelations).map(evaluationMapper::toEvaluationResource);
                    });


        }
        return Mono.error(new IllegalArgumentException("Need an Id to update a evaluation"));

    }

    public Flux<EvaluationResource> getAll() {
        return evaluationRepo.findAll().flatMap(this::getRelations).map(evaluationMapper::toEvaluationResource);
    }

    @Transactional
    public Mono<EvaluationResource> delete(Long id) {

        return evaluationRepo.findById(id)
                .flatMap(e->evaluationRepo.delete(e)
                        .then(Mono.just(e).flatMap(this::getRelations)
                                .map(evaluationMapper::toEvaluationResource)));


    }

    private Mono<Evaluation> getRelations(final Evaluation evaluation) {
        return Mono.just(evaluation)
                .zipWith(teamRepo.findById(evaluation.getTeamId()))
                .map(result -> result.getT1().setTeam(result.getT2()))
                .zipWith(userRepo.findById(evaluation.getUserId()))
                .map(result -> result.getT1().setUser(result.getT2()));
    }
    private String todayDateString() {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }
}
