package hodei.naiz.teammorale.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Hodei Eceiza
 * Date: 1/27/2022
 * Time: 14:04
 * Project: TeamMorale
 * Copyright: MIT
 */
@Configuration
public class WebAppProperties {
    @Value("${client.baseUrl}")
    private String clientUrl;

    public String getClientUrl(){
        return clientUrl;
    }
}
