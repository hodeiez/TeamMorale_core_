package hodei.naiz.teammorale.persistance.DAO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Created by Hodei Eceiza
 * Date: 1/13/2022
 * Time: 13:18
 * Project: TeamMorale
 * Copyright: MIT
 */

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EvaluationCalculations {
    private LocalDate date;
    private double energyDev;

    private double productionDev;

    private double wellBeingDev;

    private double energyAvg;

    private double productionAvg;

    private double wellBeingAvg;

}
