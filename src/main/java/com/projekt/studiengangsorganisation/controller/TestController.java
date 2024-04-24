package com.projekt.studiengangsorganisation.controller;
import org.springframework.web.bind.annotation.RestController;
import com.projekt.studiengangsorganisation.entity.TestEntity;
import com.projekt.studiengangsorganisation.service.implementation.TestServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("test/")
public class TestController {

    @Autowired
    TestServiceImpl testServ; //package-private (visibility)

    @GetMapping("sayhello")
    public String sayHello() {
        return "Hello User";
    }

    @GetMapping("gettests")
    public List<TestEntity> getTests() {
        return testServ.getTests();
    }

    @PostMapping("addtests")
    public TestEntity saveTest(@RequestBody TestEntity test) {
        //Die save Methode wird zum speichern nach dem editieren(id = id des Eintrags) und neu speichern(id = 0) eines Eintrags verwendet!!!
        return testServ.saveTest(test);
    }

    @GetMapping("gettest")
    public TestEntity getTest(String testname) {
        return testServ.getTest(testname);
    }
}