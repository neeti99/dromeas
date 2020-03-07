package edu.pec.dromeas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
public class ServiceNotImplementedException extends RuntimeException
{
    public ServiceNotImplementedException()
    {
        super();
    }

    public ServiceNotImplementedException(String message)
    {
        super(message);
    }
}
