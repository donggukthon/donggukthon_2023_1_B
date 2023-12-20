package flirting.demo.exception;

import flirting.demo.common.CustomException;
import flirting.demo.common.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    @ResponseBody
    public ResponseEntity<?> handlerException(Exception e) {
        log.error("Handing Exception : {}", e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(
                StatusCode.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(value = { CustomException.class })
    @ResponseBody
    public ResponseEntity<?> handlerCustomException(CustomException e) {
        log.error("Handing CustomException : {}", e.getErrorCode().getStatusMessage());
        return new ResponseEntity<>(
                e.getErrorCode(),
                e.getErrorCode().getHttpStatus()
        );
    }
}
