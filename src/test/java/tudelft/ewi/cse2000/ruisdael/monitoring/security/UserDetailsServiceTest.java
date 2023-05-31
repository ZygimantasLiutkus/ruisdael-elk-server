package tudelft.ewi.cse2000.ruisdael.monitoring.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent.User;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent.UserRepository;

@SpringBootTest
public class UserDetailsServiceTest {

    public static final String username = "admin";
    public static final User adminUser = new User(0L, username, "password", true, false, true, true);

    @Mock
    public UserRepository userRepository;

    @InjectMocks
    public UserDetailsService userDetailsService;

    @BeforeEach
    void setMockOutput() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(adminUser.getUsername())).thenReturn(Optional.of(adminUser));
    }

    @DisplayName("Test No User Found")
    @Test
    void noUserInRepository() {
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("someone"));
    }

    @DisplayName("Test Correct User Returned")
    @Test
    void testGetUser() {
        assertEquals((UserDetails) adminUser, userDetailsService.loadUserByUsername(username));
        verify(userRepository, atLeastOnce()).findByUsername(username);
    }
}
