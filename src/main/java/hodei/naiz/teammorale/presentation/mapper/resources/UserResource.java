package hodei.naiz.teammorale.presentation.mapper.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:22
 * Project: TeamMorale
 * Copyright: MIT
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResource {
    private String username;
    private String email;
    @Getter(onMethod_=@JsonIgnore)
    private String password;
    @Getter(onMethod_=@JsonIgnore)
    private String ignore;

}
