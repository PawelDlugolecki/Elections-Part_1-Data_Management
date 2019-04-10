package dlugolecki.pawel.repository;


import dlugolecki.pawel.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
