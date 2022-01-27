package hodei.naiz.teammorale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;


@SpringBootApplication(exclude = {
        RedisRepositoriesAutoConfiguration.class})//, SecurityAutoConfiguration.class,ReactiveSecurityAutoConfiguration.class})//TODO: take out to run security
@EnableR2dbcAuditing
public class TeamMoraleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamMoraleApplication.class, args);
    }


}
