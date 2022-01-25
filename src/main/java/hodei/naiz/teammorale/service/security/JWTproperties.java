package hodei.naiz.teammorale.service.security;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Created by Hodei Eceiza
 * Date: 1/25/2022
 * Time: 21:48
 * Project: TeamMorale
 * Copyright: MIT
 */
@Configuration
@Setter
public class JWTproperties {
    @Value("${security.key}")
    private  String key;

    @Value("${security.duration}")
    private  Integer duration;

    @Value("${security.algorithm}")
    private String algorithm;
    @Bean
    public Key getKey() {

        final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.forName(algorithm);
        final byte[] signingKeyBytes = key.getBytes();
        return new SecretKeySpec(signingKeyBytes, signatureAlgorithm.getJcaName());
    }

    public Integer getDuration(){
        return duration;
    }
    public String getAlgorithm(){
        return algorithm;
    }
    public String getKeyString(){
        return key;
    }

}
