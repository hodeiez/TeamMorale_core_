package hodei.naiz.teammorale.service.security;

import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import javax.crypto.spec.SecretKeySpec;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Hodei Eceiza
 * Date: 1/25/2022
 * Time: 00:39
 * Project: TeamMorale
 * Copyright: MIT
 */

class JWTissuerTest {


   private JWTissuer jwtIssuer;



    @BeforeEach
    private void getJWTIssuer(){
        JWTproperties jwtProperties = new JWTproperties();
        jwtProperties.setAlgorithm("HS512");
        jwtProperties.setKey("*F-J@NcRfUjXn2r5u8x/A?D(G+KbPdSgVkYp3s6v9y$B&E)H@McQfThWmZq4t7w!");
        jwtProperties.setDuration(10);
        jwtIssuer=new JWTissuer(jwtProperties);

    }

    @Test
    void itShouldValidateToken() {
        String actual = jwtIssuer.createToken(new UserAuth("name", "password", List.of("ROLE_USER")));
        Assertions.assertTrue(jwtIssuer.validateToken(actual));

        String fail="not.a.token";
        Assertions.assertFalse(jwtIssuer.validateToken(fail));
        Assertions.assertEquals(((User)jwtIssuer.getAuthentication(actual).getPrincipal()).getUsername(),"name");
        Assertions.assertEquals(jwtIssuer.getAuthentication(actual).isAuthenticated(),true);

    }
}
