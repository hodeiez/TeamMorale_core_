package hodei.naiz.teammorale.presentation.mapper.resources;

import lombok.Data;

import java.util.List;

/**
 * Created by Hodei Eceiza
 * Date: 1/18/2022
 * Time: 23:20
 * Project: TeamMorale
 * Copyright: MIT
 */
@Data
public class TeamUpdateResource {
   private List<String> membersToRemove;
   private String name;
   private List<String>membersToAdd;
   private Long userTeamId;
}
