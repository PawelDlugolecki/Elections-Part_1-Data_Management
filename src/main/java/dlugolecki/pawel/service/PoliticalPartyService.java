package dlugolecki.pawel.service;

import dlugolecki.pawel.dto.MyModelMapper;
import dlugolecki.pawel.dto.PoliticalPartyDto;
import dlugolecki.pawel.exceptions.ExceptionCode;
import dlugolecki.pawel.exceptions.MyException;
import dlugolecki.pawel.model.PoliticalParty;
import dlugolecki.pawel.repository.CandidateRepository;
import dlugolecki.pawel.repository.PoliticalPartyRepository;
import dlugolecki.pawel.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PoliticalPartyService {
    private final PoliticalPartyRepository politicalPartyRepository;
    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;
    private final MyModelMapper modelMapper;

    public void addPoliticalParty(PoliticalPartyDto politicalPartyDto) {
        try {
            if (politicalPartyDto == null) {
                throw new NullPointerException("NULL POLITICAL PARTY");
            }
            PoliticalParty politicalParty = modelMapper.fromPoliticalPartyDtoToPoliticalParty(politicalPartyDto);
            politicalPartyRepository.save(politicalParty);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADD POLITICAL PARTY" + e);
        }
    }

    public List<PoliticalPartyDto> getAllPoliticalParties() {
        try {
            return politicalPartyRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromPoliticalPartyToPoliticalPartyDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET ALL POLITICAL PARTIES: " + e);
        }
    }

    public void deletePoliticalParty(Long politicalPartyId) {
        try {
            PoliticalParty politicalParty = politicalPartyRepository.findById(politicalPartyId)
                    .orElseThrow(NullPointerException::new);
            politicalPartyRepository.delete(politicalParty);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DELETE POLITICAL PARTY: " + e);
        }
    }
}
