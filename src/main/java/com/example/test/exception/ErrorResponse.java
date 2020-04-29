package com.example.test.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {

    private HttpStatus status;
    private List<String> errors;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ErrorResponse() {
        super();
    }

    public ErrorResponse(HttpStatus status, LocalDateTime timestamp, List<String> errors) {
        super();
        this.status = status;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public ErrorResponse(HttpStatus status, LocalDateTime timestamp, String error) {
        super();
        this.status = status;
        this.timestamp = timestamp;
        errors = Collections.singletonList(error);
    }
}