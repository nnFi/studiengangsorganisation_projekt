package com.projekt.studiengangsorganisation.service.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projekt.studiengangsorganisation.entity.*;
import com.projekt.studiengangsorganisation.repository.*;
import com.projekt.studiengangsorganisation.service.TestService;


@Service
public class TestServiceImpl implements TestService{

    @Autowired TestRepository testRepo;

    @Override
    public List<TestEntity> getTests() {
        return testRepo.findAll();
    }

    @Override
    public TestEntity saveTest(TestEntity test) {
        return testRepo.save(test);
    }

    @Override
    public TestEntity getTest(String testname) {
        return testRepo.findByTestname(testname);
    }
}
