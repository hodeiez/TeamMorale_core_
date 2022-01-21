package hodei.naiz.teammorale.service;

import hodei.naiz.teammorale.domain.Team;
import hodei.naiz.teammorale.domain.User;
import hodei.naiz.teammorale.persistance.TeamRepo;
import hodei.naiz.teammorale.persistance.UserRepo;
import hodei.naiz.teammorale.presentation.mapper.TeamMapper;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamAndMembersResource;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamResource;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamUpdateResource;
import hodei.naiz.teammorale.service.publisher.EmailServiceMessage;
import hodei.naiz.teammorale.service.publisher.EmailType;
import hodei.naiz.teammorale.service.publisher.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private final PublisherService publisherService;


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
    public Mono<TeamAndMembersResource> getOne(Long teamId) {
        return teamRepo.findById(teamId).flatMap(this::setUsersInTeam).map(teamMapper::getWithMembersResource);
    }

    @Transactional
    public Mono<Long> addUserToTeam(Long userId, Long teamId) {
        return teamRepo.existsById(teamId).flatMap(teamExists -> teamExists ?
                userRepo.existsById(userId).flatMap(userExists -> userExists ?
                        teamRepo.addUserToTeam(userId, teamId)//.doOnSuccess(consume->{sendEmailsExistingUsers(userId,teamId);})
                        : Mono.error(new IllegalArgumentException("User doesn't exist")))
                : Mono.error(new IllegalArgumentException("Team doesn't exist")));
    }

    public Flux<TeamAndMembersResource> getByEmail(String email) {
        return teamRepo.getAllByEmail(email).flatMap(t -> addUserTeamsIdToTeam(t, email))
                .flatMap(this::setUsersInTeam).map(teamMapper::getWithMembersResource);
    }


    @Transactional
    public Mono<TeamAndMembersResource> addUsersToTeam(List<String> mails, Long teamId) {
       mails.forEach(m->sendEmailsToNotExistingUser(m,teamId));
        return Flux.fromIterable(mails).concatMap(mail -> userRepo.findOneByEmail(mail)
                .flatMap(u -> userRepo.userExistsInTeam(u.getId(), teamId)
                        .flatMap(isInTeam -> !isInTeam ? addUserToTeamWithEmail(mail, teamId)
                                : Mono.just(u))))
                .then(teamRepo.findById(teamId)).flatMap(this::setUsersInTeam).map(teamMapper::getWithMembersResource);
    }

    @Transactional
    public Mono<TeamAndMembersResource> createWithUsers(TeamAndMembersResource teamWithEmails, String creatorEmail) {

        return teamRepo.save(new Team().setName(teamWithEmails.getName()))
                .flatMap(t -> addUsersToTeam(teamWithEmails.getMembers(), t.getId())
                        .zipWith(addUserToTeamWithEmail(creatorEmail, t.getId()))
                        .map(res -> res.getT1().withUserTeamsId(res.getT2())));

    }


    @Transactional
    public Mono<TeamAndMembersResource> updateTeam(String email,TeamUpdateResource teamUpdateResource) {
        return  userRepo.userExistsInTeamWithUserTeamsAndEmail(teamUpdateResource.getUserTeamId(), email)
                .flatMap(exists -> exists ? teamRepo.getByUserTeamsId(teamUpdateResource.getUserTeamId()).flatMap(u -> {
            if (teamUpdateResource.getName() != null) return teamRepo.save(u.setName(teamUpdateResource.getName()));
            return Mono.just(u);
        }).flatMap(u -> {
            if (teamUpdateResource.getMembersToRemove() != null) {
                return Mono.just(teamUpdateResource.getMembersToRemove())
                        .flatMapMany(Flux::fromIterable)
                        .concatMap(emails -> teamRepo.unsubscribeUserByEmail(u.getId(), emails)).reduce((team, team2) -> team);
            }
            return Mono.just(u);
        }).flatMap(u -> {
            if (teamUpdateResource.getMembersToAdd() != null) {
                return addUsersToTeam(teamUpdateResource.getMembersToAdd(), u.getId());
            }
            return Mono.just(u).flatMap(this::setUsersInTeam).map(teamMapper::getWithMembersResource);
        }): Mono.error(new IllegalArgumentException("This user has no access to this feature")));

    }

    @Transactional
    public Mono<TeamResource> unsubscribeUserByEmail(String email, Long teamId) {
        return teamRepo.unsubscribeUserByEmail(teamId, email).map(teamMapper::getResource);
    }

    @Transactional
    public Mono<TeamResource> deleteTeamFull(String email, Long userTeamId) {
        return userRepo.userExistsInTeamWithUserTeamsAndEmail(userTeamId, email)
                .flatMap(exists -> exists ? teamRepo.getByUserTeamsId(userTeamId)
                .flatMap(t -> teamRepo.unsubscribeAll(t.getId()))
                .flatMap(team -> teamRepo.delete(team).then(Mono.just(team))).map(teamMapper::getResource)
                        : Mono.error(new IllegalArgumentException("This user has no access to this feature")));
    }

    private Mono<Team> setUsersInTeam(Team team) {
        return Mono.just(team).zipWith(userRepo.findAllByTeamId(team.getId()).collectList(), Team::setMembers);
    }

    private Mono<Team> addUserTeamsIdToTeam(Team team, String email) {
        return userRepo.findOneByEmail(email).map(User::getId)
                .flatMap(u -> Mono.just(team).zipWith(teamRepo.getUserTeamsId(u, team.getId()), Team::setUserTeamsId));
    }


    private Mono<Long> addUserToTeamWithEmail(String email, Long teamId) {
        return userRepo.findOneByEmail(email)
                .flatMap(u -> addUserToTeam(u.getId(), teamId))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Email doesn't exist")));
    }

    private void sendEmailsToNotExistingUser(String email, Long teamId){
        Mono<User> user=userRepo.findOneByEmail(email).switchIfEmpty(Mono.just(new User()));
        Mono<Team> team=teamRepo.findById(teamId);
      user.zipWith(team).doOnNext(t->publisherService.sendEmail(EmailServiceMessage.buildAddedToTeam()
                .to(email)
                .username(t.getT1().getUsername()==null?email:t.getT1().getUsername())
                .emailType(EmailType.ADDED_TO_TEAM)
                .teamName(t.getT2().getName())
                .message("if you weren't signed up when invited you need to ask to your team to be subscribed to the team again")
                .build())).subscribe();

    }


}
