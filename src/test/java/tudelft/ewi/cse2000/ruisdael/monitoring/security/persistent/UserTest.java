package tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserTest {

    public static final User adminUser = new User(0L, "admin", "password", true, false, true, true);
    public static final User demoUser = new User(0L, "demo", "password", true, false, true, false);


    @DisplayName("Test Correct Authorities returned")
    @Test
    void getAuthorities() {
        Collection<? extends GrantedAuthority> demoAuthorities = demoUser.getAuthorities();
        assertTrue(demoAuthorities.contains(new SimpleGrantedAuthority("USER")));
        assertFalse(demoAuthorities.contains(new SimpleGrantedAuthority("ADMIN")));

        Collection<? extends GrantedAuthority> adminAuthorities = adminUser.getAuthorities();
        assertTrue(adminAuthorities.contains(new SimpleGrantedAuthority("USER")));
        assertTrue(adminAuthorities.contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @DisplayName("Test UserDetails Required Methods")
    @Test
    void userDetailsMethods() {
        //Should be true if user is enabled
        assertTrue(demoUser.isAccountNonExpired());
        assertTrue(demoUser.isAccountNonLocked());

        demoUser.setEnabled(false);

        assertFalse(demoUser.isAccountNonExpired());
        assertFalse(demoUser.isAccountNonLocked());

        assertFalse(demoUser.isCredentialsNonExpired());
    }
}
