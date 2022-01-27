package hodei.naiz.teammorale.service.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * Created by Hodei Eceiza
 * Date: 1/25/2022
 * Time: 00:39
 * Project: TeamMorale
 * Copyright: MIT
 */

class JWTutilTest {


   private JWTutil jwtIssuer;



    @BeforeEach
    private void getJWTIssuer(){
        JWTproperties jwtProperties = new JWTproperties();
        jwtProperties.setAlgorithm("HS512");
        jwtProperties.setKey("*F-J@NcRfUjXn2r5u8x/A?D(G+KbPdSgVkYp3s6v9y$B&E)H@McQfThWmZq4t7w!");
        jwtProperties.setDuration(10);
        jwtIssuer=new JWTutil(jwtProperties);

    }

    @Test
    void itShouldValidateToken() {
        String actual = jwtIssuer.createToken(new UserAuth("name", "password", List.of("ROLE_USER"),true));
        Assertions.assertTrue(jwtIssuer.validateToken(actual));

        String fail="not.a.token";
        Assertions.assertFalse(jwtIssuer.validateToken(fail));
        Assertions.assertEquals(((User)jwtIssuer.getAuthentication(actual).getPrincipal()).getUsername(),"name");
        Assertions.assertEquals(jwtIssuer.getAuthentication(actual).isAuthenticated(),true);

    }
}
