package hodei.naiz.teammorale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Created by Hodei Eceiza
 * Date: 1/15/2022
 * Time: 22:51
 * Project: TeamMorale
 * Copyright: MIT
 */
@Table
@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EvaluationMaxMinCalculations {
    private int userId;
    private int maxProductionTeamId;
    private int maxEnergyTeamId;
    private int maxWellBeingTeamId;
    private int maxWellBeing;
    private int maxEnergy;
    private int maxProduction;

}
