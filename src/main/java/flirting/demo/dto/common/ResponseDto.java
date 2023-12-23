package flirting.demo.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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

}
