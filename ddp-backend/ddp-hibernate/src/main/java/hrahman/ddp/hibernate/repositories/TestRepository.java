package hrahman.ddp.hibernate.repositories;

import hrahman.ddp.hibernate.models.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Integer> {

    @Override
    List<TestEntity> findAll();
}
