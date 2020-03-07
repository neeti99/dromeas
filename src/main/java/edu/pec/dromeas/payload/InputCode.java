package edu.pec.dromeas.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InputCode
{
    //TODO see will large string cause a problem
    @NotBlank
    private String code;

    public String getCode() {
        return code;
    }

}
