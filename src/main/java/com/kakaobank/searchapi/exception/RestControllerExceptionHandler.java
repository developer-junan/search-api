package com.kakaobank.searchapi.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class RestControllerExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseVO unknownException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return getErrorResponse(ErrorTypeEnum.ERROR_9999, ex.getMessage());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseVO missingServletRequestParameterException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return getErrorResponse(ErrorTypeEnum.ERROR_0001, ex.getMessage());
    }

    @ExceptionHandler(value = {FeignException.class})
    public ResponseEntity<?> feignException(Exception ex) {
        int status = 500;
        String additionalMessage = ex.getMessage();
        if (ex instanceof FeignException.Unauthorized) {
            status = ((FeignException.Unauthorized) ex).status();
        }

        if (ex instanceof FeignException.BadRequest) {
            status = ((FeignException.BadRequest) ex).status();
        }

        if (ex instanceof FeignException.BadGateway) {
            status = ((FeignException.BadGateway) ex).status();
        }

        if (ex instanceof FeignException.GatewayTimeout) {
            status = ((FeignException.GatewayTimeout) ex).status();
        }

        return ResponseEntity.status(status)
                .body(getErrorResponse(ErrorTypeEnum.ERROR_0003, additionalMessage));
    }

    private ErrorResponseVO getErrorResponse(ErrorTypeEnum errorTypeEnum, String additionalMessage) {
        return ErrorResponseVO.builder()
                .errorCode(errorTypeEnum.getErrorCode())
                .errorMessage(errorTypeEnum.getErrorMessage())
                .additionalMessage(additionalMessage)
                .build();
    }
}
