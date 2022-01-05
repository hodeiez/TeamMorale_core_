package hodei.naiz.teammorale.presentation.mapper.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by Hodei Eceiza
 * Date: 1/5/2022
 * Time: 10:52
 * Project: TeamMorale
 * Copyright: MIT
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class TeamAndMembersResource {

    private Long id;
    private String name;
    private String startDate;
    private String lastUpdateDate;
    private List<String> members;
    private Long userTeamsId;
}
