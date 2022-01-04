package hodei.naiz.teammorale.presentation.mapper;

import hodei.naiz.teammorale.domain.Team;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Hodei Eceiza
 * Date: 1/3/2022
 * Time: 14:37
 * Project: TeamMorale
 * Copyright: MIT
 */
@Mapper(componentModel="spring")//, uses={UserMapper.class})
public abstract class TeamMapper {
    @Mapping(source = "createdDate", target = "startDate", dateFormat = "dd.MM.yy")
    public abstract TeamResource getResource(Team team);
//TODO: when create team by list done, uncomment and add TeamCreateResource
    /*
    @Mapping(source = "createdDate", target = "startDate", dateFormat = "dd.MM.yy")
    @Mapping(source = "modifiedDate", target = "lastUpdateDate", dateFormat = "dd.MM.yy")
    public abstract TeamAndMembersResource getWithMembersResource(Team team);

    protected List<String> mapToUserName(List<User> values) {
        return values.stream().map(User::getUsername).collect(Collectors.toList());

    }


    public abstract TeamCreateResource createTeam(String team);
 */
}
