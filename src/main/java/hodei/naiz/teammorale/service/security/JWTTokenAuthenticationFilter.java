package hodei.naiz.teammorale.service.security;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Created by Hodei Eceiza
 * Date: 1/25/2022
 * Time: 15:02
 * Project: TeamMorale
 * Copyright: MIT
 */
@AllArgsConstructor
public class JWTTokenAuthenticationFilter implements WebFilter {
    public static final String HEADER_PREFIX = "Bearer ";

    private final JWTutil tokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = resolveToken(exchange.getRequest());

        if (StringUtils.hasText(token)&& this.tokenProvider.validateToken(token)) {

            Authentication authentication = this.tokenProvider.getAuthentication(token);

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }
        return chain.filter(exchange);
    }

    private String resolveToken(ServerHttpRequest request) {

        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
