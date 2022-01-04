package hodei.naiz.teammorale.presentation.mapper.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:56
 * Project: TeamMorale
 * Copyright: MIT
 */
@Data
public class EvaluationResource {
    private Long id;
    private Long energy;
    @JsonProperty("well_being")
    private Long wellBeing;
    private Long production;
    private String username;
    private String team;
    private String date;

}
