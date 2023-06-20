package tudelft.ewi.cse2000.ruisdael.monitoring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Index;

@Repository
public interface IndexRepository extends JpaRepository<Index, Long> {

    boolean existsByIndexValue(String index);

    Index findByIndexValue(String index);
}
