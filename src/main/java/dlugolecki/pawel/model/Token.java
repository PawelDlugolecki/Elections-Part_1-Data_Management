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
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue
    private Long id;
    private String tokenValue;
    private LocalDateTime expirationDate;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "voter_id")
    private Voter voter;
}