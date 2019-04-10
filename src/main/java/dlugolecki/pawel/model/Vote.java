package dlugolecki.pawel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime dateTime;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "voter_id")
    private Voter voter;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

}