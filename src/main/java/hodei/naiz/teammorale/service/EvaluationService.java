package hodei.naiz.teammorale.service;

import hodei.naiz.teammorale.domain.Evaluation;
import hodei.naiz.teammorale.domain.Team;
import hodei.naiz.teammorale.domain.User;
import hodei.naiz.teammorale.persistance.DAO.EvaluationCalculations;
import hodei.naiz.teammorale.persistance.EvaluationRepo;
import hodei.naiz.teammorale.persistance.TeamRepo;
import hodei.naiz.teammorale.persistance.UserRepo;
import hodei.naiz.teammorale.presentation.mapper.EvaluationMapper;
import hodei.naiz.teammorale.presentation.mapper.resources.EvaluationResource;
import hodei.naiz.teammorale.presentation.mapper.resources.UserEvaluationCalculationsResource;
import hodei.naiz.teammorale.service.notification.Topics;
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
    private final NotificationService notificationService;

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

    /**
     * creates or updates depending on id sent in request, request is not accepted if userTeamsId, energy, production and/or well being are missing
     * @param evaluation
     * @return
     */
    @Transactional
    public Mono<EvaluationResource> createOrUpdate(Evaluation evaluation){

        return create(evaluation)
                .onErrorResume(e->e instanceof IllegalArgumentException,
                        response-> evaluationRepo.findTodayByUserTeamsId(evaluation.getUserTeamsId())
                                .map(oldE->new Evaluation()
                                        .setId(oldE.getId())
                                        .setUserTeamsId(oldE.getUserTeamsId())
                                        .setEnergy(evaluation.getEnergy())
                                        .setProduction(evaluation.getProduction())
                                        .setWellBeing(evaluation.getWellBeing()))
                                .flatMap(this::update));
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

    /**
     * get User id and Team id from userTeamsId, then listen to notification and return as evaluationResource
     * @param userTeamId
     * @return
     */
    public Flux<EvaluationResource> listenSaved(Long userTeamId) {

     return userRepo.getByUserTeamsId(userTeamId)
             .zipWith(teamRepo.getByUserTeamsId(userTeamId))
             .flatMapMany(t->notificationService.listen(Topics.EVALUATION_SAVED, t.getT1().getId(), t.getT2().getId()))
                     .flatMap(this::getRelations).map(evaluationMapper::toEvaluationResource);


    }

    //TODO: decide if validate today is today??
    public Flux<EvaluationResource> getByDateAndTeamId(Long userTeamsId) {
        return evaluationRepo.findAllByDateAndTeamId(userTeamsId).flatMap(this::getRelations).map(evaluationMapper::toEvaluationResource);
    }

    public Mono<EvaluationCalculations> getAverageByDateAndTeam(String date, Long teamId){
        return evaluationRepo.getAverageByDateAndTeam(teamId,date);
    }
    public Mono<EvaluationCalculations> getTotalAverageByTeam( Long teamId){
        return evaluationRepo.getTotalAverageByTeam(teamId);
    }

    public Flux<EvaluationCalculations> getAllAverageOfDatesByTeam( Long teamId){
        return evaluationRepo.getAllAverageOfDatesByTeam(teamId);
    }
    public Mono<UserEvaluationCalculationsResource> getUserEvaluationCalculations(Long userId){
        return userRepo.getEvaluationsTeamAverageByDate(userId).collectList().
                zipWith(userRepo.getEvaluationsMaxAndMin(userId))
                .map(result->new UserEvaluationCalculationsResource(result.getT1(),result.getT2()));
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
