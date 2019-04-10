package dlugolecki.pawel.service;

import dlugolecki.pawel.dto.CandidateDto;
import dlugolecki.pawel.dto.MyModelMapper;
import dlugolecki.pawel.exceptions.ExceptionCode;
import dlugolecki.pawel.exceptions.MyException;
import dlugolecki.pawel.model.Candidate;
import dlugolecki.pawel.model.Constituency;
import dlugolecki.pawel.model.PoliticalParty;
import dlugolecki.pawel.repository.CandidateRepository;
import dlugolecki.pawel.repository.ConstituencyRepository;
import dlugolecki.pawel.repository.PoliticalPartyRepository;
import dlugolecki.pawel.repository.VoteRepository;
import dlugolecki.pawel.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ConstituencyRepository constituencyRepository;
    private final PoliticalPartyRepository politicalPartyRepository;
    private final VoteRepository voteRepository;
    private final FileManager fileManager;
    private final MyModelMapper modelMapper;

    public void addCandidate(CandidateDto candidateDto) {
        try {
            if (candidateDto == null) {
                throw new NullPointerException("CANDIDATE IS NULL");
            }
            if (candidateDto.getAge() < 18) {
                throw new IllegalArgumentException("CANDIDATE'S AGE < 18");
            }
            if (candidateDto.getConstituencyDto() == null) {
                throw new NullPointerException("NULL CANDIDATE'S CONSTITUENCY");
            }
            if (candidateDto.getName() == null) {
                throw new NullPointerException("NULL CANDIDATE'S NAME");
            }
            if (candidateDto.getSurname() == null) {
                throw new NullPointerException("NULL CANDIDATE'S SURNAME");
            }
            if (candidateDto.getPoliticalPartyDto() == null) {
                throw new NullPointerException("NULL CANDIDATE'S POLITICAL PARTY");
            }

            Candidate candidate = modelMapper.fromCandidateDtoToCandidate(candidateDto);
            Constituency constituency = constituencyRepository
                    .findById(candidateDto.getConstituencyDto().getId())
                    .orElseThrow(NullPointerException::new);
            PoliticalParty politicalParty = politicalPartyRepository
                    .findById(candidateDto.getPoliticalPartyDto().getId())
                    .orElseThrow(NullPointerException::new);

            String filename = fileManager.addFile(candidateDto.getFile());

            candidate.setPoliticalParty(politicalParty);
            candidate.setConstituency(constituency);
            candidate.setPhoto(filename);

            candidateRepository.save(candidate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "ADD CANDIDATE: " + e);
        }
    }

    public List<CandidateDto> getAllCandidates() {
        try {
            return candidateRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromCandidateToCandidateDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET ALL CANDIDATES: " + e);
        }
    }

    public CandidateDto getOneCandidate(Long id) {
        try {
            Candidate candidate = candidateRepository.findById(id).orElseThrow(NullPointerException::new);
            return modelMapper.fromCandidateToCandidateDto(candidate);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET ONE CANDIDATE");
        }
    }

    public void deleteCandidate(Long candidateId) {
        try {
            Candidate candidate = candidateRepository.findById(candidateId)
                    .orElseThrow(NullPointerException::new);
            candidateRepository.delete(candidate);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DELETE CANDIDATE: " + e);
        }
    }

    public List<Candidate> winnersFromEachConstituency() {

        return candidateRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Candidate::getVotes, Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        constituency -> constituency.getConstituency().getName(),
                        candidate -> candidate,
                        (v1, v2) -> v1,
                        LinkedHashMap::new))
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
