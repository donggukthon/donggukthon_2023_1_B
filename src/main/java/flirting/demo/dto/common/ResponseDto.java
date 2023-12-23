package flirting.demo.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import flirting.demo.exception.CommonException;
import flirting.demo.exception.ErrorCode;
import jakarta.annotation.Nullable;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

@Getter
public class ResponseDto<T> {
    // HttpStatus는 클라에게 중복 전달될 필요 없으므로 @JsonIgnore
    @JsonIgnore
    private HttpStatus httpStatus;
    private final Boolean success;
    private final T data;
    private ExceptionDto exception;

    public ResponseDto(final HttpStatus httpStatus, final Boolean success,
    @Nullable T data, ExceptionDto exception){
        this.httpStatus = httpStatus;
        this.success = success;
        this.data = data;
        this.exception = exception;
    }

    // Response에서 사용되는 일반적인 응답에 대한 메소드 정의
    // static factory method
    // factory method로 객체를 생성해서 반환해 주고 있음 -> 객체 생성 메소드는 클래스에만 정의되는 것으로 충분
    // 인스턴스마다 팩토리 메소드가 정의되어 있는 것은 불필요

    // OK
    public static <T> ResponseDto<T> ok(@Nullable final T data){
        return new ResponseDto<>(HttpStatus.OK, true, data, null);
    }

    // CREATED
    public static <T> ResponseDto<T> created(@Nullable final T data){
        return new ResponseDto<>(HttpStatus.CREATED, true, data, null);
    }

    // fail: non-defined error
    public static ResponseDto<Object> fail(CommonException e){
        return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, false, null, new ExceptionDto(e.getErrorCode()));
    }

    // fail - bad request
    public static ResponseDto<Object> fail(MethodArgumentNotValidException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, new ExceptionDto(ErrorCode.INVALID_ARGUMENT));
    }

    public static ResponseDto<Object> fail(ConstraintViolationException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, new ExceptionDto(ErrorCode.INVALID_ARGUMENT));
    }

    public static ResponseDto<Object> fail(MissingServletRequestParameterException e) {
        // Todo : MissingRequestValueException vs. MissingServletRequestParameterException
        // 이름만 보면 전자가 맞을 것 같은데 왜?
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, new ExceptionDto(ErrorCode.MISSING_REQUEST_PARAMETER));
    }
}
