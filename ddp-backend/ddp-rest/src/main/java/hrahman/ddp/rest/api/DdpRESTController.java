package hrahman.ddp.rest.api;

import hrahman.ddp.hibernate.models.FailedBankFileInfo;
import hrahman.ddp.hibernate.services.FailedBankFileInfoService;
import hrahman.ddp.rest.dto.NYCViolationsFileInfo;
import hrahman.ddp.rest.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController("/")
@Validated
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

    @PostMapping("/nyc/parking-camera/violations/download")
    public ResponseEntity<String> downloadNycParkingAndCameraViolations(@Valid @RequestBody NYCViolationsFileInfo nycViolationsFileInfo) {

        Utilities.downloadNycViolationsFile(nycViolationsFileInfo);

        return new ResponseEntity<>("Acknowledged", HttpStatus.ACCEPTED);

    }

}
