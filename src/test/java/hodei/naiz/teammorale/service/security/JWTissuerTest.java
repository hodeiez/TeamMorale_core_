package hodei.naiz.teammorale.service.security;

import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Hodei Eceiza
 * Date: 1/25/2022
 * Time: 00:39
 * Project: TeamMorale
 * Copyright: MIT
 */
class JWTissuerTest {

    JWTissuer jwtIssuer;

    private String key = "*F-J@NcRfUjXn2r5u8x/A?D(G+KbPdSgVkYp3s6v9y$B&E)H@McQfThWmZq4t7w!";
    private String algorithm = "HS512";
    private Integer duration=5;

    @BeforeEach
    private void getJWTIssuer(){

        final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.forName(algorithm);
        final byte[] signingKeyBytes = Base64.encodeBase64(key.getBytes());
        jwtIssuer = new JWTissuer(new SecretKeySpec(signingKeyBytes, signatureAlgorithm.getJcaName()),
                Duration.ofMinutes(duration));
    }

    @Test
    void itShouldValidateToken() {
        String actual = jwtIssuer.createToken(new UserAuth("name", "password","role"));
        Assertions.assertTrue(jwtIssuer.validateToken(actual));

        String fail="not.a.token";
        Assertions.assertFalse(jwtIssuer.validateToken(fail));

    }
}
