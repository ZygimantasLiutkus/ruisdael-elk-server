package tudelft.ewi.cse2000.ruisdael.monitoring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tudelft.ewi.cse2000.ruisdael.monitoring.entity.Alert;


@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    /*
     * Spring fills in the methods.
     * This repository keeps all the device alerts.
     */
}
