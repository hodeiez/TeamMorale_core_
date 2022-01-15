package hodei.naiz.teammorale.persistance;

import hodei.naiz.teammorale.domain.EvaluationCalculations;
import hodei.naiz.teammorale.domain.EvaluationMaxMinCalculations;
import hodei.naiz.teammorale.domain.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:17
 * Project: TeamMorale
 * Copyright: MIT
 */
@Repository
public interface UserRepo extends R2dbcRepository<User, Long> {
    @Query("SELECT public.user.id,public.user.username,public.user.email,public.user.created_date,public.user.modified_date FROM user_teams JOIN public.user ON public.user.id=user_id where user_teams.id=:userTeamsId;")
    Mono<User> getByUserTeamsId(Long userTeamsId);

    @Query("SELECT * FROM public.user WHERE email=:email")
    Mono<User> findOneByEmail(String email);

    @Query("SELECT public.user.id, public.user.username, public.user.email FROM user_teams join public.user on public.user.id=user_id where team_id=:teamId")
    Flux<User> findAllByTeamId(Long teamId);

    @Query("SELECT EXISTS(SELECT * FROM user_teams WHERE team_id=:teamId AND user_id=:userId);")
    Mono<Boolean> userExistsInTeam(Long userId, Long teamId);

    Mono<User> findOneByEmailAndPassword(String email, String password);

    @Query("SELECT date(created_date), ROUND(stddev_pop(energy),2) as energy_dev,ROUND(stddev_pop(well_being),2) as well_being_dev,ROUND(stddev_pop(production),2) as production_dev,ROUND(AVG(energy),2) as energy_avg, ROUND(AVG(well_being),2) AS well_being_avg,ROUND(AVG(production),2) AS production_avg FROM evaluation WHERE user_id=:userId group by date(created_date) order by date(created_date);")
    Flux<EvaluationCalculations> getEvaluationsTeamAverageByDate(Long userId);

    @Query("SELECT maxPR.user_id,maxPR.team_id as max_production_team_id, maxPR.max as max_production,maxEN.team_id as max_energy_team_id,\n" +
            "maxEN.max as max_energy,maxWB.max as max_well_being,maxWB.team_id as max_well_being_team_id,minPR.team_id as min_production_team_id, minPR.min as min_production,minEN.team_id as min_energy_team_id,\n" +
            "minEN.min as min_energy,minWB.min as min_well_being,minWB.team_id as min_well_being_team_id\n" +
            "from (select MIN(production),team_id,user_id from evaluation where user_id=:userId \n" +
            "group by team_id, production,user_id order by production asc limit 1) as minPR,\n" +
            "(select MIN(energy),team_id, user_id from evaluation where user_id=:userId group by team_id,\n" +
            " energy,user_id order by energy asc limit 1) as minEN,\n" +
            "(select MIN(well_being),team_id, user_id from evaluation where user_id=:userId group by team_id,\n" +
            " well_being,user_id order by well_being asc limit 1) as minWB,\n" +
            " (select MAX(production),team_id,user_id from evaluation where user_id=:userId \n" +
            "group by team_id, production,user_id order by production desc limit 1) as maxPR,\n" +
            "(select MAX(energy),team_id, user_id from evaluation where user_id=:userId group by team_id,\n" +
            " energy,user_id order by energy desc limit 1) as maxEN,\n" +
            "(select MAX(well_being),team_id, user_id from evaluation where user_id=:userId group by team_id,\n" +
            " well_being,user_id order by well_being desc limit 1) as maxWB;")
    Mono<EvaluationMaxMinCalculations> getEvaluationsMaxAndMin(Long userId);
}
