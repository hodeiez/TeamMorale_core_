package hodei.naiz.teammorale.service.security;

import hodei.naiz.teammorale.presentation.mapper.resources.UserLoginResource;
import hodei.naiz.teammorale.presentation.mapper.resources.UserResource;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Hodei Eceiza
 * Date: 1/24/2022
 * Time: 23:22
 * Project: TeamMorale
 * Copyright: MIT
 */
@Component
@AllArgsConstructor
public class JWTissuer {
    private final Logger log = LoggerFactory.getLogger(JWTissuer.class);
    private final JWTproperties jwtProperties;


    public String createToken(UserAuth user){

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("Authority",user.getAuthorities())
                .signWith(jwtProperties.getKey())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(jwtProperties.getDuration()))))
                .compact();


    }
    public Authentication getAuthentication(String token) {

        if (token.isEmpty() || !validateToken(token)) {

            throw new BadCredentialsException("Invalid token");
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("Authority").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getKey()).build().parseClaimsJws(authToken);

            return true;
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
            log.trace(e.toString(),e);
        }

        return false;
    }
    public String getUserEmail(String token){
        return getAuthentication(token.substring(7)).getName();
    }
    public String createTokenWhenLogin(UserLoginResource userLogin){
        return createToken(new UserAuth(userLogin.getEmail(), userLogin.getPassword(), List.of("ROLE_USER")));
    }
    public String createTokenFromUser(hodei.naiz.teammorale.domain.User user){
        return createToken(new UserAuth(user.getEmail(), user.getPassword(), List.of("ROLE_USER")));
    }
}
