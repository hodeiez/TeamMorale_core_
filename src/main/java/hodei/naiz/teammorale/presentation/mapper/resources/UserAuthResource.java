package hodei.naiz.teammorale.presentation.mapper.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Hodei Eceiza
 * Date: 1/25/2022
 * Time: 23:59
 * Project: TeamMorale
 * Copyright: MIT
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAuthResource {
    private String username;
    private String email;
    private String token;
}
