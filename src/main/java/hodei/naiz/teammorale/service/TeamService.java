package hodei.naiz.teammorale.service;

import hodei.naiz.teammorale.domain.Team;
import hodei.naiz.teammorale.persistance.TeamRepo;
import hodei.naiz.teammorale.presentation.mapper.TeamMapper;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

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

        return teamRepo.findById(id).flatMap(t->teamRepo.delete(t).then(Mono.just(t).map(teamMapper::getResource)));


    }
}
