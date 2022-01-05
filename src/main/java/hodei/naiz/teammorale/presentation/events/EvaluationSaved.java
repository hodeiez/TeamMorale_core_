package hodei.naiz.teammorale.presentation.events;

import hodei.naiz.teammorale.presentation.mapper.resources.EvaluationResource;
import lombok.Value;

/**
 * Created by Hodei Eceiza
 * Date: 1/5/2022
 * Time: 12:38
 * Project: TeamMorale
 * Copyright: MIT
 */
@Value
public class EvaluationSaved implements Event{
    EvaluationResource evaluation;
}
