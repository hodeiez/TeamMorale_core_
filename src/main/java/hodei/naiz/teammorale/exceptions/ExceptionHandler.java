package hodei.naiz.teammorale.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Created by Hodei Eceiza
 * Date: 1/3/2022
 * Time: 22:29
 * Project: TeamMorale
 * Copyright: MIT
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandler implements WebExceptionHandler {


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        if(ex.getClass().getCanonicalName().equalsIgnoreCase(DataIntegrityViolationException.class.getCanonicalName())){
            return transcryptMessage(exchange,"Operation not accepted by DB, check if you sent duplicates values, or if there is any reference in the database");}
            else{
        return transcryptMessage(exchange,ex.getMessage());}
    }

    private Mono<Void> transcryptMessage(ServerWebExchange exchange,String message){
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}
