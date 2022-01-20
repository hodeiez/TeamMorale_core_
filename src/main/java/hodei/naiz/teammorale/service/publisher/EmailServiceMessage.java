package hodei.naiz.teammorale.service.publisher;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Hodei Eceiza
 * Date: 1/20/2022
 * Time: 10:07
 * Project: TeamMorale
 * Copyright: MIT
 */
@Data
@AllArgsConstructor
public class EmailServiceMessage {

    private String to;
    private String message;
    private String username;

}
