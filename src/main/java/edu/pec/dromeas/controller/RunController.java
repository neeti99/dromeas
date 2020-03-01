package edu.pec.dromeas.controller;

import edu.pec.dromeas.payload.InputCode;
import edu.pec.dromeas.service.RunService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/run")

public class RunController
{
    private RunService runService;

    public RunController(RunService runService)
    {
        this.runService = runService;
    }

    @PostMapping({"/python3","/py3"})
    public ResponseEntity<?> runPython3Code(@RequestBody @Valid InputCode code)
    {
        return runService.runPython3(code);
    }

    //TODO Python2
    @PostMapping({"python2","/py2"})
    public ResponseEntity<?> runPython2Code(@RequestBody @Valid InputCode code)
    {
        return runService.runPython2(code);
    }

    //TODO C
    @PostMapping({"/c"})
    public ResponseEntity<?> runCCode(@RequestBody @Valid InputCode code)
    {
        return runService.runC(code);
    }

    //TODO CPP
    @PostMapping({"/cpp"})
    public ResponseEntity<?> runCppCode(@RequestBody @Valid InputCode code)
    {
        return runService.runCpp(code);
    }

    //TODO JavaScript
    @PostMapping({"/javascript","js"})
    public ResponseEntity<?> runJavascriptCode(@RequestBody @Valid InputCode code)
    {
        return runService.runJavaScript(code);
    }

    //TODO Java
    @PostMapping({"/java"})
    public ResponseEntity<?> runJavaCode(@RequestBody @Valid InputCode code)
    {
        return runService.runJava(code);
    }
}
