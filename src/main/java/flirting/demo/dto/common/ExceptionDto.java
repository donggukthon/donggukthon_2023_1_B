package flirting.demo.dto.common;

import flirting.demo.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ExceptionDto {
    private final Integer code;
    private final String message;

    public ExceptionDto(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
