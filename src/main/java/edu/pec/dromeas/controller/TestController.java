package edu.pec.dromeas.controller;

import edu.pec.dromeas.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController
{
    final TestService testService;

    public TestController(TestService testService)
    {
        this.testService = testService;
    }

    @GetMapping("/stat")
    public ResponseEntity<?> systemStat()
    {
        return testService.systemStat();
    }

    //TODO can be used to test all language against a reference code
    @GetMapping({"","/"})
    public ResponseEntity<?> systemTest()
    {
        return testService.systemTest();
    }

  /*  @GetMapping("/tessa")
    public ResponseEntity<?> runTessaract()
    {
        return testService.runTessaract();
    }*/

}
