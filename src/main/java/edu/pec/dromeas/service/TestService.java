package edu.pec.dromeas.service;

import edu.pec.dromeas.exception.ServerException;
import edu.pec.dromeas.exception.ServiceNotImplementedException;
import edu.pec.dromeas.payload.SystemStat;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TestService
{
    final String BASE = new File("").getAbsolutePath();
    final String TESSADATA = BASE + "\\tessa";
    public ResponseEntity<?> systemTest()
    {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Hello World");
    }

    public ResponseEntity<?> systemStat()
    {
        SystemStat stat = new SystemStat();

        stat.setMaxMemory(Runtime.getRuntime().maxMemory());
        stat.setFreeMemory(Runtime.getRuntime().freeMemory());
        stat.setTotalMemory((Runtime.getRuntime().totalMemory()));
        stat.setProcessors(Runtime.getRuntime().availableProcessors());

        return ResponseEntity.status(HttpStatus.OK).body(stat);
    }

    public ResponseEntity<?> runTessaract()
    {
        Tesseract tesseract = new Tesseract();

        //instantiate the Tesseract object and set the data path to
        // the LSTM (Long Short-Term Memory) models pre-trained for your use.

        System.out.println(TESSADATA);
        tesseract.setDatapath(TESSADATA);

        try
        {
            String testFilePath = BASE+"\\test\\code.jpg";
            System.out.println(testFilePath);

            File toScan = new File(testFilePath);

            System.out.println(toScan.getName());
            System.out.println(toScan.length());

            String result = (tesseract.doOCR(toScan));

            return ResponseEntity.ok(result);
        }

        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServerException("Failed to extract text", e);
        }

    }
}
