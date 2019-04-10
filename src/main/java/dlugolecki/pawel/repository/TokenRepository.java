package dlugolecki.pawel.repository;

import dlugolecki.pawel.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
