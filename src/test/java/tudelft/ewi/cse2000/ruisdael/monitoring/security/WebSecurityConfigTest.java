package tudelft.ewi.cse2000.ruisdael.monitoring.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootTest
public class WebSecurityConfigTest {

    public static final String testPassword = "testString";

    @Autowired
    public WebSecurityConfig webSecurityConfig;

    @Mock
    public HttpSecurity http;

    @BeforeEach
    void setMockOutput() throws Exception {
        when(http.authenticationProvider(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.formLogin(any())).thenReturn(http);
        when(http.logout(any())).thenReturn(http);
        when(http.build()).thenReturn(new DefaultSecurityFilterChain(null));
    }

    @DisplayName("Check if the SecurityFilterChain is created")
    @Test
    void securityFilterChain() throws Exception {
        SecurityFilterChain securityFilterChain = webSecurityConfig.securityFilterChain(http);
        assertNotNull(securityFilterChain);
    }

    /**
     * If another encoder is being used, then using a BCrypt encoder here to try to match the password will result in a failure.
     */
    @DisplayName("Check if BCrypt Encoder is implemented")
    @Test
    void encoder() {
        PasswordEncoder implemented = webSecurityConfig.encoder();

        //Test
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(testPassword, implemented.encode(testPassword)));
    }

    @DisplayName("Check if an Auth Provider is created")
    @Test
    void authProvider() {
        DaoAuthenticationProvider authProvider = webSecurityConfig.customAuthProdiver();
        assertNotNull(authProvider);
    }
}
