package edu.pec.dromeas.payload;

import lombok.Data;

@Data
public class Result
{
    private String result;

    public void setResult(String result) {
        this.result = result;
    }
}
