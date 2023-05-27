package tudelft.ewi.cse2000.ruisdael.monitoring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/resources/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticate")
                        .defaultSuccessUrl("/overview", true)
                        .failureUrl("/login?failure=true")
                )
                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }

    /**
     * DISABLE THIS METHOD WHEN BUILDING OR RUNNING IN PRODUCTION
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails demoUser =
                User.withDefaultPasswordEncoder()
                        .username("demo")
                        .password("demo")
                        .roles("ADMIN")
                        .build();
        return new InMemoryUserDetailsManager(demoUser);
    }
}
