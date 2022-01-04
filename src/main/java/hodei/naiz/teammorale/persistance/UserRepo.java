package hodei.naiz.teammorale.persistance;

import hodei.naiz.teammorale.domain.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:17
 * Project: TeamMorale
 * Copyright: MIT
 */
@Repository
public interface UserRepo extends R2dbcRepository<User,Long> {
}
