package hodei.naiz.teammorale.service.security;

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

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by Hodei Eceiza
 * Date: 1/24/2022
 * Time: 23:22
 * Project: TeamMorale
 * Copyright: MIT
 */
@AllArgsConstructor
public class JWTissuer {
    private final Logger log = LoggerFactory.getLogger(JWTissuer.class);
    private final Key key;
    private final Duration duration;



    public String createToken(UserAuth user){

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorities",user.getAuthorities())
                .signWith(key)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(duration)))
                .compact();


    }
    public Authentication getAuthentication(String token) {
        if (token.isEmpty() || !validateToken(token)) {
            throw new BadCredentialsException("Invalid token");
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
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
                    .setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
            log.trace(e.toString(),e);
        }
        return false;
    }
}
