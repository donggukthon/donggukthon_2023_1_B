package flirting.demo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteGuessRequest {
    Long memberId;
    Long selectedMemberId;
    Long questionId;
    Long groupId;

}
