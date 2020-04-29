package com.example.test.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ValidatorException.class})
    public ResponseEntity<Object> validatorException(ValidatorException ex) {

        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getError().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }

        for (ObjectError error : ex.getError().getGlobalErrors()) {
            errors.add(error.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                errors
        );

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> notFoundException(NotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                LocalDateTime.now(),
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({NoContentException.class})
    public ResponseEntity<Object> noContentException(NoContentException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NO_CONTENT,
                LocalDateTime.now(),
                ""
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatus status,
                                                         WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
            logger.info(
                    "== Error Field : " + error.getField()
                            + " , Value : " + error.getRejectedValue()
                            + " , Message : " + error.getDefaultMessage()
            );
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getDefaultMessage());
            logger.info("== Error Message: " + error.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                errors
        );

        return handleExceptionInternal(ex, errorResponse, headers, errorResponse.getStatus(), request);
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(),
                "error occurred"
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                ex.getParameterName() + " 파라미터가 없습니다."
        );

        return new ResponseEntity<>(
                errorResponse,
                new HttpHeaders(),
                errorResponse.getStatus()
        );
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                   WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                ex.getName() + " 의 타입이 잘못되었습니다."
        );

        return new ResponseEntity<>(
                errorResponse,
                new HttpHeaders(),
                errorResponse.getStatus()
        );
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex,
                                                            WebRequest request) {

        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                errors
        );

        return new ResponseEntity<>(
                errorResponse,
                new HttpHeaders(),
                errorResponse.getStatus()
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        logger.error("== Http Message Not Readable aaaaa !!! " + ex);

        return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }
}