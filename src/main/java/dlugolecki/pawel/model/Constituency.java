package dlugolecki.pawel.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "constituencies")
public class Constituency {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "constituency")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Candidate> candidates;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "constituency")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Voter> voters;
}