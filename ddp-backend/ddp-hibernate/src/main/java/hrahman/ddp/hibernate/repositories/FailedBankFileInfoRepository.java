package hrahman.ddp.hibernate.repositories;

import hrahman.ddp.hibernate.models.FailedBankFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FailedBankFileInfoRepository extends JpaRepository<FailedBankFileInfo, Integer> {

    @Query(nativeQuery = true,
            value = "select * from ddp_schema.failed_bank_file_info where filename like '%_unprocessed' and http_code = 200 and processor_flag = 'U'")
    Set<FailedBankFileInfo> findAllByUnprocessedAndOkHttpCodeAndUnProcessorFlag();
}
