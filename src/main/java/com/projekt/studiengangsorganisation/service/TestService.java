package com.projekt.studiengangsorganisation.service;

import java.util.List;
import com.projekt.studiengangsorganisation.entity.TestEntity;

public interface TestService {

    List<TestEntity> getTests();
    TestEntity saveTest(TestEntity test);
    TestEntity getTest(String testname);
}
