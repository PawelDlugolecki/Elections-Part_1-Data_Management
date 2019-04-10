package dlugolecki.pawel.service;

import dlugolecki.pawel.dto.MyModelMapper;
import dlugolecki.pawel.dto.VoterDto;
import dlugolecki.pawel.exceptions.ExceptionCode;
import dlugolecki.pawel.exceptions.MyException;
import dlugolecki.pawel.model.Constituency;
import dlugolecki.pawel.model.Token;
import dlugolecki.pawel.model.Voter;
import dlugolecki.pawel.repository.ConstituencyRepository;
import dlugolecki.pawel.repository.TokenRepository;
import dlugolecki.pawel.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class VoterService {
    private final VoterRepository voterRepository;
    private final ConstituencyRepository constituencyRepository;
    private final TokenRepository tokenRepository;
    private final MyModelMapper modelMapper;

    public VoterDto addVoter(VoterDto voterDto) {
        try {
            if (voterDto == null) {
                throw new NullPointerException("VOTER IS NULL");
            }
            if (voterDto.getAge() < 18) {
                throw new IllegalArgumentException("VOTER'S AGE < 18");
            }
            if (voterDto.getEducation() == null) {
                throw new NullPointerException("NULL VOTER'S EDUCATION");
            }
            if (voterDto.getGender() == null) {
                throw new NullPointerException("NULL VOTER'S GENDER");
            }
            if (voterDto.getConstituencyDto() == null) {
                throw new NullPointerException("NULL VOTER'S CONSTITUENCY");
            }

            Constituency constituency = constituencyRepository.findById(
                    voterDto.getConstituencyDto().getId())
                    .orElseThrow(NullPointerException::new);

            TokenService tokenService = new TokenService(tokenRepository, modelMapper);
            Token token = Token.builder()
                    .tokenValue(tokenService.newToken())
                    .expirationDate(LocalDateTime.now().plusHours(3))
                    .build();

            Voter voter = modelMapper.fromVoterDtoToVoter(voterDto);
            voter.setConstituency(constituency);

            token.setVoter(voter);
            voterRepository.save(voter);
            tokenRepository.save(token);
            return modelMapper.fromVoterToVoterDto(voter);

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADD VOTER: " + e);
        }
    }
}
