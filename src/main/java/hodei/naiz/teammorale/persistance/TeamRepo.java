package hodei.naiz.teammorale.persistance;

import hodei.naiz.teammorale.domain.Team;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Hodei Eceiza
 * Date: 1/3/2022
 * Time: 14:31
 * Project: TeamMorale
 * Copyright: MIT
 */
@Repository
public interface TeamRepo extends R2dbcRepository<Team,Long> {
}
