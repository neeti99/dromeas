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
    final String BASE = new File("").getAbsolutePath() + "\\scratch\\";
    final Long UPPER = (long)1.0E9;

    final String PYTHON_HOME = "C:\\ProgramData\\Anaconda3\\python.exe";
    final String C_HOME = "";

    final String PYTHON = ".py";
    final String C = ".c";
    final String CPP = ".cpp";

    public ResponseEntity<?> runPython3(InputCode input)
    {
        String code = input.getCode();
        //System.out.println(code);
        File file = createFile(code,PYTHON);

        try //Execute the local run-file
        {
            String command = getPython3CommandLine(file);
            //System.out.println(command);
            Process process = Runtime.getRuntime().exec(command);

            return executionResult(process, file);
        }

        catch (IOException e)
        {
            throw new ServerException("Failed to run the python file", e);
        }
    }

    public ResponseEntity<?> runPython2(InputCode input)
    {
        throw new ServiceNotImplementedException();
    }

    public ResponseEntity<?> runC(InputCode input)
    {
        String code = input.getCode();
        System.out.println(code);
        File file = createFile(code,C);

        try //Execute the local run-file
        {
            String command = getCCommandLine(file);

            //System.out.println(command);
            String customCommand = BASE+"\\command.bat";
            Process process = Runtime.getRuntime().exec(customCommand);

            return executionResult(process, file);
        }

        catch (IOException e)
        {
            e.printStackTrace();
            throw new ServerException("Failed to run the c file");
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


    private String getPython3CommandLine(File file)
    {
        String fileName = file.getAbsolutePath();
        String result = PYTHON_HOME + " \""+fileName+"\"";

        return result;
    }

    private String getCCommandLine(File file)
    {
        String fileName = FilenameUtils.removeExtension(file.getName());
        System.out.println(fileName);

        String temp = "C:\\MinGW\\bin\\gcc.exe";
        //String result = temp+"\' gcc "+fileName+".c -o "+fileName+" && \""+BASE+"\""+fileName+"\'";
        String result = "gcc -o "+fileName+" "+fileName+".c&"+fileName+".exe";

        return result;

    }

    private File createFile(String code, String fileType)
    {
        try
        {
            String file;
            long hash = random();
            file = BASE+"temp"+hash+fileType;
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println(code);
            writer.close();

            return new File(file);
        }

        catch (IOException e)
        {
            throw new ServerException("Failed to create load run file", e);
        }
    }

    private Long random()
    {
        return (long)(Math.random() * UPPER);
    }


}
