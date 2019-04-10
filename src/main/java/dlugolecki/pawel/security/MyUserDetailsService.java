package dlugolecki.pawel.security;

import dlugolecki.pawel.exceptions.ExceptionCode;
import dlugolecki.pawel.exceptions.MyException;
import dlugolecki.pawel.model.security.Role;
import dlugolecki.pawel.model.security.User;
import dlugolecki.pawel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Qualifier("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            if (username == null) {
                throw new NullPointerException("USERNAME IS NULL");
            }

            User user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username));

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getEnabled(),
                    true,
                    true,
                    true,
                    getAuthorites(user.getRole())
            );
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SECURITY, e.getMessage());
        }
    }

    public Collection<GrantedAuthority> getAuthorites(Role... roles) {
        return Arrays
                .stream(roles)
                .map(r -> new SimpleGrantedAuthority(r.getFullName()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
