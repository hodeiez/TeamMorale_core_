package hodei.naiz.teammorale.persistance;

import hodei.naiz.teammorale.domain.Team;
import hodei.naiz.teammorale.domain.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Created by Hodei Eceiza
 * Date: 1/3/2022
 * Time: 14:31
 * Project: TeamMorale
 * Copyright: MIT
 */
@Repository
public interface TeamRepo extends R2dbcRepository<Team,Long> {
    @Query("INSERT INTO user_teams (user_id,team_id) VALUES (:userId,:teamId) RETURNING id;")
    Mono<Long> addUserToTeam(Long userId, Long teamId);
    @Query("SELECT team.id,team.name,team.created_date,team.modified_date FROM user_teams JOIN team ON team.id=team_id where user_teams.id=:userTeamsId;")
    Mono<Team> getByUserTeamsId (Long userTeamsId);
}