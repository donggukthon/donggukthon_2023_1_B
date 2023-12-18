package flirting.demo.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    OK(HttpStatus.OK, "Y200", "ok"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"N500", "Internal server error"),
    ;

    private final HttpStatus httpStatus;
    private final String statusCode;
    private final String statusMessage;
}
