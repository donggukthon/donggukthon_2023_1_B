package flirting.demo.exception;

import flirting.demo.dto.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    @ResponseBody
    public ResponseDto<?> handlerException(Exception e) {
        // 서버 측 문제
        //
        log.error("Handing Exception : {}", e.getMessage());
        e.printStackTrace();
        return ResponseDto.fail(new CommonException(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(value = { CommonException.class })
    @ResponseBody
    public ResponseDto<?> handlerCustomException(CommonException e) {
        log.error("Handing CustomException : {}", e.getErrorCode().getMessage());
        return ResponseDto.fail(e);
    }
}
