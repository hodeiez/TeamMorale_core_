package hodei.naiz.teammorale.presentation.mapper.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hodei.naiz.teammorale.persistance.DAO.EvaluationCalculations;
import hodei.naiz.teammorale.persistance.DAO.EvaluationMaxMinCalculations;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by Hodei Eceiza
 * Date: 1/17/2022
 * Time: 09:51
 * Project: TeamMorale
 * Copyright: MIT
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class UserEvaluationCalculationsResource {
    private List<EvaluationCalculations> evaluationCalculations;
    private EvaluationMaxMinCalculations maxMinCalculations;
}
