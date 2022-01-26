package hodei.naiz.teammorale.presentation.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

/**
 * Created by Hodei Eceiza
 * Date: 1/26/2022
 * Time: 14:33
 * Project: TeamMorale
 * Copyright: MIT
 */
@AllArgsConstructor
public class UnauthorizedEvent implements Event{
    private String message;
}
