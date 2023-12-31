package hrahman.ddp.hibernate.services;

import hrahman.ddp.hibernate.models.FailedBankFileInfo;
import hrahman.ddp.hibernate.repositories.FailedBankFileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FailedBankFileInfoService {

    @Autowired
    private FailedBankFileInfoRepository failedBankFileInfoRepository;

    public List<FailedBankFileInfo> getAll() {
        return failedBankFileInfoRepository.findAll();
    }

    public Set<FailedBankFileInfo> getUnprocessedFiles() {
        return failedBankFileInfoRepository.findAllByUnprocessedAndOkHttpCodeAndUnProcessorFlag();
    }

    public FailedBankFileInfo save(Map<String, Object> fileInfo) {
        FailedBankFileInfo failedBankFileInfo = new FailedBankFileInfo();

        failedBankFileInfo.setUrl(fileInfo.getOrDefault("url", "NA").toString());
        failedBankFileInfo.setDownloadDir(fileInfo.getOrDefault("download_dir", "NA").toString());
        failedBankFileInfo.setFileName(fileInfo.getOrDefault("filename", "NA").toString());
        failedBankFileInfo.setHttpStatusCode((Integer) fileInfo.getOrDefault("http_code", -1));
        failedBankFileInfo.setProcessorFlag(fileInfo.getOrDefault("processor_flag", "U").toString().charAt(0));

        return failedBankFileInfoRepository.saveAndFlush(failedBankFileInfo);
    }

    public FailedBankFileInfo update(FailedBankFileInfo failedBankFileInfo) {
        return failedBankFileInfoRepository.saveAndFlush(failedBankFileInfo);
    }
}
