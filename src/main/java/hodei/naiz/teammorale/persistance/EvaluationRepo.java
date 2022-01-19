package hodei.naiz.teammorale.persistance;

import hodei.naiz.teammorale.domain.Evaluation;
import hodei.naiz.teammorale.persistance.DAO.EvaluationCalculations;
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
    @Query("SELECT * FROM evaluation  WHERE date(created_date)=current_date AND user_teams_id=:userTeamsId;")
    Mono<Evaluation> findTodayByUserTeamsId(Long userTeamsId);
    @Query("SELECT date(created_date),stddev_pop(energy) as energy_dev,stddev_pop(well_being) as well_being_dev,stddev_pop(production) " +
            "as production_dev,AVG(energy) as energy_avg, AVG(well_being) AS well_being_avg,AVG(production) AS " +
            "production_avg,AVG(team_id) AS team_id FROM evaluation WHERE date(created_date)=date(:date) AND team_id=:teamId group by date(created_date)")
    Mono<EvaluationCalculations> getAverageByDateAndTeam(Long teamId, String date);

    @Query("SELECT date(created_date), ROUND(stddev_pop(energy),2) as energy_dev,ROUND(stddev_pop(well_being),2) as well_being_dev," +
            "ROUND(stddev_pop(production),2) as production_dev,ROUND(AVG(energy),2) as energy_avg, ROUND(AVG(well_being),2) AS well_being_avg," +
            "ROUND(AVG(production),2) AS production_avg,AVG(team_id) AS team_id FROM evaluation WHERE team_id=:teamId group by date(created_date) order by date(created_date);")
    Flux<EvaluationCalculations> getAllAverageOfDatesByTeam(Long teamId);

    @Query("SELECT stddev_pop(energy) as energy_dev,stddev_pop(well_being) as well_being_dev," +
            "stddev_pop(production) as production_dev,AVG(energy) as energy_avg, AVG(well_being) AS well_being_avg," +
            "AVG(production) AS production_avg,AVG(team_id) AS team_id FROM evaluation WHERE team_id=:teamId ")
    Mono<EvaluationCalculations> getTotalAverageByTeam(Long teamId);
    Mono<Evaluation> deleteAllByTeamId(Long teamId);
}
