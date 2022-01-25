package hodei.naiz.teammorale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.security.authentication.ReactiveAuthenticationManagerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;


@SpringBootApplication(exclude = {
        RedisRepositoriesAutoConfiguration.class})//, SecurityAutoConfiguration.class,ReactiveSecurityAutoConfiguration.class})//TODO: take out to run security
@EnableR2dbcAuditing
public class TeamMoraleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamMoraleApplication.class, args);
    }

}
