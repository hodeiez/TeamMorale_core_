package hodei.naiz.teammorale.service;

import hodei.naiz.teammorale.domain.Team;
import hodei.naiz.teammorale.domain.User;
import hodei.naiz.teammorale.persistance.TeamRepo;
import hodei.naiz.teammorale.persistance.UserRepo;
import hodei.naiz.teammorale.presentation.mapper.TeamMapper;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamAndMembersResource;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Hodei Eceiza
 * Date: 1/3/2022
 * Time: 14:33
 * Project: TeamMorale
 * Copyright: MIT
 */
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepo teamRepo;
    private final TeamMapper teamMapper;
    private final UserRepo userRepo;

    /*Basic CRUDs*/
    @Transactional
    public Mono<TeamResource> create(Team team) {
        if (team.getId() != null)
            return Mono.error(new IllegalArgumentException("Id must be null"));
        return teamRepo.save(team).map(teamMapper::getResource);
    }

    @Transactional
    public Mono<TeamResource> update(Team team) {
        if (team.getId() != null) {
            return teamRepo.findById(team.getId())
                    .flatMap(t -> {
                        t.setName(team.getName());
                        t.setModifiedDate(LocalDateTime.now());
                        return teamRepo.save(t).map(teamMapper::getResource);
                    });

        }
        return Mono.error(new IllegalArgumentException("Need an Id to update a team"));

    }

    public Flux<TeamResource> getAll() {
        return teamRepo.findAll().map(teamMapper::getResource);
    }

    @Transactional
    public Mono<TeamResource> delete(Long id) {
        return teamRepo.findById(id)
                .flatMap(t -> teamRepo.delete(t)
                        .then(Mono.just(t)
                                .map(teamMapper::getResource)));
    }

    /*extra operations*/

       @Transactional
    public Mono<Long> addUserToTeam(Long userId, Long teamId) {
        return teamRepo.existsById(teamId).flatMap(teamExists -> teamExists ?
                        userRepo.existsById(userId).flatMap(userExists -> userExists ?
                               teamRepo.addUserToTeam(userId, teamId)
                                : Mono.error(new IllegalArgumentException("User doesn't exist")))
                        : Mono.error(new IllegalArgumentException("Team doesn't exist")));
    }
    public Flux<TeamAndMembersResource> getByEmail(String email){
          return teamRepo.getAllByEmail(email).flatMap(t->addUserTeamsIdToTeam(t,email)).flatMap(this::setUsersInTeam).map(teamMapper::getWithMembersResource);
    }

    //TODO: we need to get the userTeamId, maybe get userId from request
    @Transactional
    public Mono<TeamAndMembersResource> addUsersToTeam(List<String> mails, Long teamId){

           return Flux.fromIterable(mails).concatMap(mail->userRepo.findOneByEmail(mail)
                   .flatMap(u->userRepo.userExistsInTeam(u.getId(),teamId)
                           .flatMap(exists->!exists?addUserToTeam(u.getId(),teamId)
                                   :Mono.just(u))))
                   //.concatMap(mail->addUserToTeamWithEmail(mail,teamId))
                   .then(teamRepo.findById(teamId)).flatMap(this::setUsersInTeam).map(teamMapper::getWithMembersResource);
    }

    @Transactional
    public Mono<TeamAndMembersResource> createWithUsers(TeamAndMembersResource teamWithEmails, String creatorEmail){

     return teamRepo.save(new Team().setName(teamWithEmails.getName()))
                .flatMap(t->addUsersToTeam(teamWithEmails.getMembers(), t.getId())
                        .zipWith(addUserToTeamWithEmail(creatorEmail,t.getId()))
                        .map(res->res.getT1().withUserTeamsId(res.getT2())));

    }
    private Mono<Team> setUsersInTeam(Team team) {
        return Mono.just(team).zipWith(userRepo.findAllByTeamId(team.getId()).collectList(), Team::setMembers);
    }
    private Mono<Team> addUserTeamsIdToTeam(Team team,String email){
       return userRepo.findOneByEmail(email).map(User::getId).flatMap(u->Mono.just(team).zipWith(teamRepo.getUserTeamsId(u,team.getId()),Team::setUserTeamsId));
        //   return Mono.just(team).zipWith(teamRepo.getUserTeamsId(userRepo.findOneByEmail(email),team.getId()),Team::setUserTeamsId);
    }
    //@Transactional
    private Mono<Long> addUserToTeamWithEmail(String email,Long teamId){
        return userRepo.findOneByEmail(email)
                .flatMap(u->addUserToTeam(u.getId(),teamId))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Email doesn't exist")));
    }



}
