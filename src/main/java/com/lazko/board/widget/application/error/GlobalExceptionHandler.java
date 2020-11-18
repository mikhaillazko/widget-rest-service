package com.lazko.board.widget.application.error;

import com.lazko.board.widget.domain.exception.InvalidArgValueException;
import com.lazko.board.widget.domain.exception.NotFoundEntityException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        var errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors())
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        for (ObjectError error : ex.getBindingResult().getGlobalErrors())
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid input arguments", errors);
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({NotFoundEntityException.class})
    protected ResponseEntity<Object> handleNotFoundEntity(NotFoundEntityException ex, WebRequest request) {
        var entityId = ex.getEntityId();
        var message = String.format("Entity with id=%s not found", entityId);
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, message, entityId.toString());
        return new ResponseEntity(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidArgValueException.class})
    protected ResponseEntity<Object> handleInvalidArgValue(InvalidArgValueException ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getArgumentName());
        return new ResponseEntity(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
