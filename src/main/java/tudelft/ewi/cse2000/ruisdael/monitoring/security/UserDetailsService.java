package tudelft.ewi.cse2000.ruisdael.monitoring.security;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent.User;
import tudelft.ewi.cse2000.ruisdael.monitoring.security.persistent.UserRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> foundUser = userRepository.findByUsername(username);

        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist.");
        }

        return (UserDetails) foundUser.get();
    }
}
