package hrahman.ddp.hibernate.services;

import hrahman.ddp.hibernate.models.TestEntity;
import hrahman.ddp.hibernate.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public List<TestEntity> getAll() {
        return testRepository.findAll();
    }

    public TestEntity save(TestEntity testEntity) {
        return testRepository.save(testEntity);
    }
}
