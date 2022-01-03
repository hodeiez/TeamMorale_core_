package hodei.naiz.teammorale.presentation.mapper.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Hodei Eceiza
 * Date: 1/3/2022
 * Time: 14:38
 * Project: TeamMorale
 * Copyright: MIT
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class TeamResource {
    private Long id;
    private String name;
    private String startDate;


}
