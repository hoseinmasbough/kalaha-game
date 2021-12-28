package com.bol.assignment.game.exception;

import com.bol.assignment.game.common.MessageConstant;
import com.bol.assignment.game.dto.ErrorMessageOutput;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.InvalidObjectException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionControllerAdvice
{

    private static final String JSON_PARSE_EXCEPTION = "JsonParseException:";

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<Object> resourceNotFoundHandler(ResourceNotFoundException ex)
    {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorMessageOutput error = new ErrorMessageOutput(MessageConstant.RESOURCE_NOT_FOUND.getMessage(), details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<Object> handleBusinessValidation(BusinessValidationException ex)
    {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorMessageOutput error = new ErrorMessageOutput(MessageConstant.BUSINESS_VALIDATION_ERROR.getMessage(), details);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex)
    {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex)
    {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorMessageOutput error = new ErrorMessageOutput(MessageConstant.INVALID_ARGUMENT.getMessage(), details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex)
    {
        List<String> details = new ArrayList<>();
        details.add(Objects.requireNonNull(ex.getRootCause()).getMessage());
        ErrorMessageOutput error = new ErrorMessageOutput(MessageConstant.ARGUMENT_TYPE_MISMATCH.getMessage(), details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex)
    {
        List<String> details = new ArrayList<>();
        String rootCause = ex.getLocalizedMessage();
        if (ex.getCause() instanceof JsonParseException)
        {
            details.add(rootCause.split(JSON_PARSE_EXCEPTION)[1]);
        }
        else
        {
            details.add(ex.getLocalizedMessage());
        }
        ErrorMessageOutput error = new ErrorMessageOutput(MessageConstant.INVALID_PAYLOAD.getMessage(), details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex)
    {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorMessageOutput error = new ErrorMessageOutput(MessageConstant.INTERNAL_SERVER_ERROR.getMessage(), details);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
