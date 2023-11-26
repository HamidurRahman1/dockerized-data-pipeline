package hrahman.ddp.rest.api;

import hrahman.ddp.hibernate.models.FailedBankFileInfo;
import hrahman.ddp.hibernate.services.FailedBankFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController("/")
public class DdpRESTController {

    @Autowired
    private FailedBankFileInfoService failedBankFileInfoService;

    @GetMapping("/")
    public String landing() {
        return "DDP REST API";
    }

    @PostMapping("/bank/fileInfo")
    public ResponseEntity<Void> saveBankFileInfo(@RequestBody Map<String, Object> fileInfo) {

        failedBankFileInfoService.save(fileInfo);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/bank/fileInfo/all")
    public ResponseEntity<List<FailedBankFileInfo>> getAllFileInfo() {

        return new ResponseEntity<>(failedBankFileInfoService.getAll(), HttpStatus.OK);
    }


}
