package hodei.naiz.teammorale.persistance;

import hodei.naiz.teammorale.domain.Evaluation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
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

}
