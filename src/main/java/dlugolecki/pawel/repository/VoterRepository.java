package dlugolecki.pawel.repository;


import dlugolecki.pawel.model.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoterRepository extends JpaRepository<Voter, Long> {
}
