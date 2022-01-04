package hodei.naiz.teammorale.presentation.mapper;

import hodei.naiz.teammorale.domain.Evaluation;
import hodei.naiz.teammorale.presentation.mapper.resources.EvaluationResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:55
 * Project: TeamMorale
 * Copyright: MIT
 */
@Mapper(componentModel="spring", uses={UserMapper.class,TeamMapper.class})
public abstract class EvaluationMapper {
    @Mapping(source="createdDate",target="date",dateFormat = "dd.MM.yy")
    @Mapping(target="username",source="user.username")
    @Mapping(target="team",source="team.name")
    public abstract EvaluationResource toEvaluationResource(Evaluation evaluation);
}
