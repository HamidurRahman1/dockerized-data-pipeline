package hrahman.ddp.hibernate.repositories;

import hrahman.ddp.hibernate.models.FailedBankFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedBankFileInfoRepository extends JpaRepository<FailedBankFileInfo, Integer> {
}
