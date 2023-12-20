package flirting.demo.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteGuessRequest {
    Long memberId;
    Long selectedMemberId;
    Long questionId;
    Long groupId;

}
