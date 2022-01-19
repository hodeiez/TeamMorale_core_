package hodei.naiz.teammorale.persistance;

import hodei.naiz.teammorale.domain.Evaluation;
import hodei.naiz.teammorale.domain.Team;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
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
    @Query("SELECT team.* FROM user_teams JOIN team ON team.id=team_id WHERE user_id=(SELECT id AS user_id FROM public.user WHERE public.user.email=:email);")
    Flux<Team> getAllByEmail(String email);
    @Query("SELECT id FROM user_teams WHERE user_id=:userId AND team_id=:teamId;")
    Mono<Long> getUserTeamsId(Long userId,Long teamId);
    @Query("WITH deleted as (DELETE FROM user_teams WHERE user_id=(SELECT id FROM public.user WHERE email=:email) AND team_id=:teamId RETURNING team_id), evaluationsdeleted as (DELETE FROM evaluation WHERE user_id=(SELECT id FROM public.user WHERE email=:email) AND team_id=:teamId RETURNING team_id) SELECT * FROM team WHERE id=(SELECT team_id FROM deleted);")
    Mono<Team> unsubscribeUserByEmail(Long teamId,String email);
    @Query("WITH deleted AS(DELETE FROM user_teams WHERE team_id=:teamId RETURNING team_id),evaluationsdeleted AS (DELETE FROM evaluation WHERE team_id=:teamId RETURNING team_id) SELECT * FROM team WHERE id=(SELECT team_id FROM deleted);")
    Mono<Team>unsubscribeAll(Long teamId);

}
