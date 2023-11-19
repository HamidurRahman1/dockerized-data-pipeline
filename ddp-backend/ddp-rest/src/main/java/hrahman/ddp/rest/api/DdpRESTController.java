package hrahman.ddp.rest.api;

import hrahman.ddp.hibernate.models.TestEntity;
import hrahman.ddp.hibernate.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class DdpRESTController {

    @Autowired
    private TestService testService;

    @GetMapping("/")
    public String land() {
        return "Landed";
    }

    @GetMapping("/save")
    public ResponseEntity<TestEntity> create() {

        TestEntity testEntity = new TestEntity();
        testEntity.setValue("Setting " + LocalDateTime.now());

        return new ResponseEntity<>(testService.save(testEntity), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TestEntity>> getAll() {

        return new ResponseEntity<>(testService.getAll(), HttpStatus.OK);
    }


}
