package hodei.naiz.teammorale.presentation.mapper.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Hodei Eceiza
 * Date: 1/5/2022
 * Time: 12:00
 * Project: TeamMorale
 * Copyright: MIT
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserLoginResource {
    public String email;
    public String password;
    public String oldPassword;
}
