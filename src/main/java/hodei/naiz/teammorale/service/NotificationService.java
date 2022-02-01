package hodei.naiz.teammorale.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hodei.naiz.teammorale.domain.Evaluation;
import hodei.naiz.teammorale.service.notification.Topics;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hodei Eceiza
 * Date: 1/5/2022
 * Time: 12:11
 * Project: TeamMorale
 * Copyright: MIT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final static Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final ConnectionFactory connectionFactory;
    private PostgresqlConnection connection;
    private final Set<Topics> watchedTopics = new HashSet<>();

    public Flux<Evaluation> listen(final Topics topic, Long userId, Long teamId) {
        logger.info("listening to get having userId=> " + userId + " and teamId=> " + teamId);
        if (!watchedTopics.contains(topic)) {
            synchronized (watchedTopics) {
                if (!watchedTopics.contains(topic)) {
                    logger.info("adding topic");
                    watchedTopics.add(topic);
                    listenStatement(topic);

                }
            }
        }

        return getConnection()
                .getNotifications()
                .filter(notification -> notification.getName().equals(topic.name()) && notification.getParameter() != null)
                .handle((notification, stringSynchronousSink) -> {
                    final String resp = notification.getParameter();
                    logger.info("raw response" + resp);
                    if (resp != null) {
                        try {
                            Evaluation evaluation = createObjectMapper().readValue(resp, Evaluation.class);

                            if (!evaluation.getUserId().equals(userId) && evaluation.getTeamId().equals(teamId)) {
                                stringSynchronousSink.next(evaluation);
                                log.info("catching info with userId " + userId + " and teamId " + teamId);
                            } else {
                                log.info("listening to with userId " + userId + " and teamId " + teamId);
                                listenStatement(topic);
                            }

                        } catch (Exception e) {
                            Mono.error(e);

                        }
                    }
                });
    }
    public void unListen(final Topics topic) {
        if (!watchedTopics.contains(topic)) {
            synchronized (watchedTopics) {
                if (!watchedTopics.contains(topic)) {
                    logger.info("unsubscribing from  topic");
                    watchedTopics.add(topic);
                    unListenStatement(topic);

                }
            }
        }
    }
    @PreDestroy
    private void preDestroy() {

        this.getConnection().close().subscribe();
    }

    private void listenStatement(final Topics topic) {
        getConnection().createStatement(String.format("LISTEN \"%s\"", topic)).execute().subscribe();
    }

    private void unListenStatement(final Topics topic){
        getConnection().createStatement(String.format("UNLISTEN \"%s\"", topic)).execute().subscribe();

    }

    @PostConstruct
    private PostgresqlConnection getConnection() {
        if (connection == null) {
            synchronized (NotificationService.class) {
                if (connection == null) {
                    logger.info("connecting to POSTGRES");
                    connection = Mono.from(connectionFactory.create())
                            .cast(PostgresqlConnection.class)
                            .block();
                }


            }
        }

        return connection;
    }

    private ObjectMapper createObjectMapper() {

        return new ObjectMapper().registerModule(new JavaTimeModule());
    }
}
