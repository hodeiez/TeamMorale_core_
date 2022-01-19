package hodei.naiz.teammorale.persistance.DAO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Hodei Eceiza
 * Date: 1/15/2022
 * Time: 22:51
 * Project: TeamMorale
 * Copyright: MIT
 */
@Data
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
    private int minProductionTeamId;
    private int minEnergyTeamId;
    private int minWellBeingTeamId;
    private int minWellBeing;
    private int minEnergy;
    private int minProduction;
    private String maxProductionTeamName;
    private String maxEnergyTeamName;
    private String maxWellBeingTeamName;
    private String minProductionTeamName;
    private String minEnergyTeamName;
    private String minWellBeingTeamName;

}
