package net.app.savable.security;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtTest {

    @Value("${jwt.secret}")
    private String secretKey;

    @Test
    void 시크릿키_존재_확인(){
        Assert.assertNotNull(secretKey);
    }
}
