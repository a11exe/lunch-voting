package ru.alabra.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.alabra.voting.util.ValidationUtil;
import ru.alabra.voting.util.exception.ErrorInfo;
import ru.alabra.voting.util.exception.ErrorType;
import ru.alabra.voting.util.exception.NotFoundException;
import ru.alabra.voting.util.exception.VotingTimeExpiredException;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private final Logger logger = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    private final ValidationUtil validationUtil;

    @Autowired
    public ExceptionInfoHandler(ValidationUtil validationUtil) {
        this.validationUtil = validationUtil;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.VALIDATION_ERROR);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorInfo handleAccessDeniedException(HttpServletRequest req, AccessDeniedException e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.APP_ERROR);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, ErrorType.DATA_NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, MethodArgumentNotValidException e) {
        return logAndGetErrorInfo(req, e, ErrorType.VALIDATION_ERROR);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(VotingTimeExpiredException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, VotingTimeExpiredException e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.VOTE_REPEAT_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.APP_ERROR);
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = validationUtil.getRootCause(e);

        if (logException) {
            logger.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            logger.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }

        return new ErrorInfo(req.getRequestURL(), errorType, rootCause.getLocalizedMessage());
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, MethodArgumentNotValidException e, ErrorType errorType) {
        List<String> details = new ArrayList<>();
        BindingResult result = e.getBindingResult();
        result.getFieldErrors().forEach(fieldError -> {
            String msg = fieldError.getField() + ": " + fieldError.getDefaultMessage();
            details.add(msg);
        });

        return new ErrorInfo(req.getRequestURL(), errorType, String.join("; ", details));
    }
}