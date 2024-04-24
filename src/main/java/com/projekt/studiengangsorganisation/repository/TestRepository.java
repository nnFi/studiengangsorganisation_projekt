package com.projekt.studiengangsorganisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.projekt.studiengangsorganisation.entity.TestEntity;


@Repository
public interface TestRepository extends JpaRepository<TestEntity,Long>{
    TestEntity findByTestname(String testname);
}
