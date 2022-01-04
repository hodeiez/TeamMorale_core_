package hodei.naiz.teammorale.persistance;

import hodei.naiz.teammorale.domain.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:17
 * Project: TeamMorale
 * Copyright: MIT
 */
@Repository
public interface UserRepo extends R2dbcRepository<User,Long> {
    @Query("SELECT public.user.id,public.user.username,public.user.email,public.user.created_date,public.user.modified_date FROM user_teams JOIN public.user ON public.user.id=user_id where user_teams.id=:userTeamsId;")
    Mono<User> getByUserTeamsId (Long userTeamsId);
    @Query("select * from public.user where email=:email")
    Mono<User> findOneByEmail(String email);
}
