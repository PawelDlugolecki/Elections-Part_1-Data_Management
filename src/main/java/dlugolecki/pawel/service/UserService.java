package dlugolecki.pawel.service;

import dlugolecki.pawel.exceptions.ExceptionCode;
import dlugolecki.pawel.exceptions.MyException;
import dlugolecki.pawel.model.security.User;
import dlugolecki.pawel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerNewUser(User user) {
        try {
            if (user == null) {
                throw new NullPointerException("USER IS NULL");
            }

            if (!Objects.equals(user.getPassword(), user.getPasswordConfirmation())) {
                throw new IllegalArgumentException("PASSWORDS DON'T MATCH");
            }

            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
