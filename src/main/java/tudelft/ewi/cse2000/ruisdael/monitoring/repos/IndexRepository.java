package tudelft.ewi.cse2000.ruisdael.monitoring.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Index;

@Repository
public interface IndexRepository extends JpaRepository<Index, Long> {

    boolean existsByIndex(String index);

    Index findByIndex(String index);
}
