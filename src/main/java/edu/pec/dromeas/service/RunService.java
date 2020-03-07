package edu.pec.dromeas.service;

import edu.pec.dromeas.exception.ServerException;
import edu.pec.dromeas.exception.ServiceNotImplementedException;
import edu.pec.dromeas.payload.InputCode;
import edu.pec.dromeas.payload.Result;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;


@Service
public class RunService
{
     final String BASE = new File("").getAbsolutePath() + "/scratch/";

    final Long UPPER = (long)1.0E9;

    final String PYTHON_HOME = "C:\\ProgramData\\Anaconda3\\python.exe";
    final String C_HOME = "";

    final String PYTHON = ".py";
    final String C = ".c";
    final String CPP = ".cpp";


    //TODO error handling, code doesn't compile, infinite loop, include header
    public ResponseEntity<?> runPython3(InputCode input)
    {
        String code = input.getCode();
        //System.out.println(code);
        File dir = createDir(code,PYTHON);

        try //Execute the local run-file
        {
            ProcessBuilder execute = new ProcessBuilder("python3", "code.py");
            execute.directory(dir);
            Process process = execute.start();
            process.waitFor();
            return executionResult(process, dir);
        }
        catch (InterruptedException e){
            e.printStackTrace();
            throw new ServerException("Failed to run the python file");

        }
        catch (IOException e)
        {
            throw new ServerException("Failed to run the python file", e);
        }
    }

    //Duplicate improvemnts from py3
    public ResponseEntity<?> runPython2(InputCode input)
    {
        String code = input.getCode();
        //System.out.println(code);
        File dir = createDir(code,PYTHON);

        try //Execute the local run-file
        {
            ProcessBuilder execute = new ProcessBuilder("python2", "code.py");
            execute.directory(dir);
            Process process = execute.start();
            process.waitFor();
            return executionResult(process, dir);
        }
        catch (InterruptedException e){
            e.printStackTrace();
            throw new ServerException("Failed to run the python file");

        }
        catch (IOException e)
        {
            throw new ServerException("Failed to run the python file", e);
        }
    }

    //TODO error handling, code doesn't compile, infinite loop, include header
    public ResponseEntity<?> runC(InputCode input)
    {
        //Step 1 - Create required directory structure
        String code = input.getCode();
        File dir = createDir(code,C);

        //Step 2 - Execute the local run-file
        try
        {
            ProcessBuilder compile = new ProcessBuilder("gcc", "code.c");
            compile.directory(dir);

            Process temp = compile.start();
            temp.waitFor();
            //TODO does waitFor wait till termination
            //  -can we use it to wait for 10 seconds and them kill the process
            //  - can we see memory 

            System.out.println(temp.exitValue());


            ProcessBuilder execute = new ProcessBuilder("./a.out");
            execute.directory(dir);

            Process process = execute.start();
            process.waitFor();

            System.out.println(process.exitValue());

            return executionResult(process, dir);
        }

        catch (IOException e)
        {
            e.printStackTrace();
            throw new ServerException("Failed to run the c file");
        }

        catch (InterruptedException e)
        {
            e.printStackTrace();
            throw new ServerException("Process Terminated");
        }
    }

    public ResponseEntity<?> runCpp(InputCode input)
    {
        throw new ServiceNotImplementedException();
    }

    public ResponseEntity<?> runJava(InputCode input)
    {
        throw new ServiceNotImplementedException();
    }

    public ResponseEntity<?> runJavaScript(InputCode input)
    {
        throw new ServiceNotImplementedException();
    }

    private ResponseEntity<?> executionResult(Process process, File file)
    {
        try
        {
            String text = "";
            String temp = "";

            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((temp = in.readLine()) != null)
            {
                System.out.println(temp);
                if(text != "")
                    text = text + "\n" + temp;
                else
                    text = temp;
            }


            //TODO account for failure of deleting local file
            //  -maybe create a repo of failed deletes and run a cleanup daemon

            //FIXME delete the directory, possible solution shell command
            if(!file.delete())
                System.out.println("!! CLEANUP FAIL "+file.getName()+" !!");

            Result result = new Result();
            result.setResult(text);

            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        catch (IOException e)
        {
            throw new ServerException("Failed to read the result",e);
        }

    }

    private Long random()
    {
        return (long)(Math.random() * UPPER);
    }

    private File createDir(String code, String fileType) {

        //Code for directory creation
        String dirPath = BASE;
        long hash = random();
        dirPath += "dir" + hash;

        File file = new File(dirPath);
        boolean flag = file.mkdir();

        if (flag) {
            System.out.println("Directory Succefully created!!");
        }
        else {
            throw new ServerException("File Creation Failed");
        }


        //Code for adding codeFile in the dir
        try{
            String filePath = file.getAbsolutePath()+"/code"+fileType;

            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            writer.println(code);
            writer.close();

            File codeFile = new File(filePath);

            if (!codeFile.exists()) {
                throw new ServerException("File Creation Failed");
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
            throw new ServerException("Failed to create load run file", e);
        }

        return file;
    }
}
