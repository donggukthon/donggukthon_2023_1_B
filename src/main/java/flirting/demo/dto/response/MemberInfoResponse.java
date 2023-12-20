package flirting.demo.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MemberInfoResponse {
    private MemberResponse memberResponse;
    private Long memberId;
    private int snowflake;

}
