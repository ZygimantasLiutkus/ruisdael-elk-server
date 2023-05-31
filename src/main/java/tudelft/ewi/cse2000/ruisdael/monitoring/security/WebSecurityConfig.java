package tudelft.ewi.cse2000.ruisdael.monitoring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Configures the Security Chain for the web dashboard.
     * Sets the authentication provider to use the H2 Database for user accounts.
     * Permits all access to the login form and static resource files.
     * Other resources must be authenticated first.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(customAuthProdiver())
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/resources/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticate")
                        .defaultSuccessUrl("/overview", true)
                        .failureUrl("/login?error=true")
                )
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .permitAll()
                        .logoutRequestMatcher(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/logout")));
        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Create a basic Authentication Provider using the H2 database for user accounts and credentials.
     * Sets the password encoder to BCrypt to avoid storing plaintext passwords.
     */
    @Bean
    public DaoAuthenticationProvider customAuthProdiver() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }
}
