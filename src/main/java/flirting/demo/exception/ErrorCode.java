package flirting.demo.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_ARGUMENT(4000, HttpStatus.OK.BAD_REQUEST, "유효하지 않은 인자"),
    MISSING_REQUEST_PARAMETER(4001, HttpStatus.BAD_REQUEST, "필수 파라미터 누락"),


    ACCESS_DENIED(4030, HttpStatus.INTERNAL_SERVER_ERROR, "접근 권한 거부"),


    MEMBER_NOT_FOUND(4040, HttpStatus.NOT_FOUND, "조회된 멤버 없음"),
    GROUP_NOT_FOUND(4041, HttpStatus.NOT_FOUND, "조회된 그룹 없음"),


    INTERNAL_SERVER_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러 발생"),
    GROUP_ALREADY_JOINED(5001, HttpStatus.INTERNAL_SERVER_ERROR, "이미 가입된 그룹"),
    ;

    private final Integer code;
    // Todo
    // ExeptionDto 생성 시 code, message만 get하여 생성
    // 따라서 ErrorCode의 값은 직접 클라에게 반환되지 않으므로 JsonIgnore 처리 하지 않아도 괜찮을 듯 -> 확인
    @JsonIgnore
    private final HttpStatus httpStatus;
    private final String message;
}
