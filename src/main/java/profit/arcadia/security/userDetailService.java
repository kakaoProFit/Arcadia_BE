package profit.arcadia.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class userDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email){
        try {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalAccessException((email)));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
