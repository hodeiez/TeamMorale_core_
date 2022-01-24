package hodei.naiz.teammorale.presentation.mapper;

import hodei.naiz.teammorale.domain.User;
import hodei.naiz.teammorale.presentation.mapper.resources.UserResource;
import org.mapstruct.Mapper;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:22
 * Project: TeamMorale
 * Copyright: MIT
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResource toUserResource(User user);
}
