package hodei.naiz.teammorale.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.client.SSLMode;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import reactor.util.annotation.NonNull;

/**
 * Created by Hodei Eceiza
 * Date: 1/3/2022
 * Time: 10:45
 * Project: TeamMorale
 * Copyright: MIT
 */
@Configuration
public class R2dbcConfiguration extends AbstractR2dbcConfiguration {
    @Value("${database.name}")
    private String database;

    @Value("${database.host}")
    private String host;

    @Value("${database.port}")
    private int port;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    @Override
    @Bean
    @NonNull
    public ConnectionFactory connectionFactory() {

        return new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration
                .builder()
                .host(host)
                .port(port)
                .username(username)
                .password(password)
                .database(database).sslMode(SSLMode.REQUIRE)
                .build());



    }

}
