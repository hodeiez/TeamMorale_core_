package hodei.naiz.teammorale.presentation.mapper;

import hodei.naiz.teammorale.domain.Team;
import hodei.naiz.teammorale.domain.User;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamAndMembersResource;
import hodei.naiz.teammorale.presentation.mapper.resources.TeamResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Hodei Eceiza
 * Date: 1/3/2022
 * Time: 14:37
 * Project: TeamMorale
 * Copyright: MIT
 */
@Mapper(componentModel="spring", uses={UserMapper.class})
public abstract class TeamMapper {
    @Mapping(source = "createdDate", target = "startDate", dateFormat = "dd.MM.yy")
    public abstract TeamResource getResource(Team team);

    @Mapping(source = "createdDate", target = "startDate", dateFormat = "dd.MM.yy")
    @Mapping(source = "modifiedDate", target = "lastUpdateDate", dateFormat = "dd.MM.yy")
    @Mapping(target="members",source="members", qualifiedByName = "members")
    @Mapping(target="membersEmail",source="members", qualifiedByName="membersEmail")
    public abstract TeamAndMembersResource getWithMembersResource(Team team);

    @Named("members")
    protected List<String> mapToUserName(List<User> values) {
        return values.stream().map(User::getUsername).collect(Collectors.toList());

    }
    @Named("membersEmail")
    protected List<String> mapToEmail(List<User> values) {
        return values.stream().map(User::getEmail).collect(Collectors.toList());

    }



    //public abstract TeamCreateResource createTeam(String team);

}
