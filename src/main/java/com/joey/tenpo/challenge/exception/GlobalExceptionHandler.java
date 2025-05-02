package com.joey.tenpo.challenge.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalServiceException.class)
    public ProblemDetail externalServiceException(ExternalServiceException exception) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(400), exception.getMessage());
        errorDetail.setProperty("description", "External Service Fail");
        log.error(exception.getMessage());
        return errorDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail exception(Exception exception) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(500), "Internal server error");
        errorDetail.setProperty("description", "Unknown internal server error");
        log.error("Error trace: {0}", exception);
        return errorDetail;
    }
}
