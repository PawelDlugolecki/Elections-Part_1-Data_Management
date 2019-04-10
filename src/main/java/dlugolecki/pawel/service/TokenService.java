package dlugolecki.pawel.service;

import dlugolecki.pawel.dto.MyModelMapper;
import dlugolecki.pawel.dto.TokenDto;
import dlugolecki.pawel.exceptions.ExceptionCode;
import dlugolecki.pawel.exceptions.MyException;
import dlugolecki.pawel.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final MyModelMapper modelMapper;

    public String newToken() {
        Random rnd = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            token.append(String.valueOf(rnd.nextInt(9)));
        }
        return token.toString();
    }

    public TokenDto getTokenById(Long id) {
        try {
            if (id == null) {
                throw new NullPointerException("OBJECT IS NULL");
            }
            return tokenRepository
                    .findAll()
                    .stream()
                    .filter(t -> t.getVoter().getId().equals(id))
                    .map(modelMapper::fromTokenToTokenDto)
                    .findFirst()
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET TOKEN BY VOTER ID: " + e);
        }
    }
}
