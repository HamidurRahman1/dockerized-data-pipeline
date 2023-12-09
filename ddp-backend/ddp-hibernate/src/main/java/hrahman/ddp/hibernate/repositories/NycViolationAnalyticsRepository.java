package hrahman.ddp.hibernate.repositories;

import hrahman.ddp.hibernate.models.NycViolationAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NycViolationAnalyticsRepository extends JpaRepository<NycViolationAnalytics, Integer> {
}
