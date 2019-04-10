package dlugolecki.pawel.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "political_parties")
public class PoliticalParty {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "politicalParty")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Candidate> candidates;
}