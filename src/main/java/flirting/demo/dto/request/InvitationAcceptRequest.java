package flirting.demo.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InvitationAcceptRequest {
    Long memberId;
    Long groupId;

    @Builder
    public InvitationAcceptRequest(Long memberId, Long groupId) {
        this.memberId = memberId;
        this.groupId = groupId;
    }

}
