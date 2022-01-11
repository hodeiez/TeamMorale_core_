package hodei.naiz.teammorale.persistance;

import hodei.naiz.teammorale.domain.Evaluation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:51
 * Project: TeamMorale
 * Copyright: MIT
 */
public interface EvaluationRepo extends R2dbcRepository<Evaluation,Long> {
    @Query("SELECT EXISTS(select * from evaluation where user_teams_id=:userTeamId and date(created_date)=date(:date));")
    Mono<Boolean> evaluationExists(Long userTeamId, String date);
    @Query("SELECT * FROM evaluation WHERE date(created_date)=current_date AND team_id=(SELECT team_id FROM user_teams WHERE id=:userTeamsId) AND NOT user_id=(SELECT user_id FROM user_teams WHERE id=:userTeamsId)")
    Flux<Evaluation> findAllByDateAndTeamId(Long userTeamsId);
}
