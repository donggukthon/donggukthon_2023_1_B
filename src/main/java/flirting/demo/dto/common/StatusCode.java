package flirting.demo.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    OK(HttpStatus.OK, "Y200", "ok"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"N500", "Internal server error"),

    /**
     * 질문이 7개가 아닐시
     */
    QUESTION_CNT_NOT_SEVEN(HttpStatus.INTERNAL_SERVER_ERROR,"N501", "There are not 7 questions"),
    NO_OTHER_MEMBERS_IN_GROUP(HttpStatus.INTERNAL_SERVER_ERROR, "N502", "There are no other member in group"),
    MYSELF_IN_OPTIONS(HttpStatus.INTERNAL_SERVER_ERROR, "N503", "There is user in vote options"),
    NO_SELECTED_QUESTION(HttpStatus.INTERNAL_SERVER_ERROR, "N504", "There is no selected questions"),
    NO_SELECTED_VOTE(HttpStatus.INTERNAL_SERVER_ERROR, "N505", "There is no votes selected by question"),
    SNOWFLAKE_NOT_ENOUGH(HttpStatus.INTERNAL_SERVER_ERROR, "N506", "There is no enough snowflakes")
    ;

    @JsonIgnore
    private final HttpStatus httpStatus;
    private final String statusCode;
    private final String statusMessage;
}
