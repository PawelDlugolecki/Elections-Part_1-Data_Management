package dlugolecki.pawel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteDto {

    private Long id;
    private LocalDateTime dateTime;
    private CandidateDto candidateDto;
    private VoterDto voterDto;
}