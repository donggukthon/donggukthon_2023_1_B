package flirting.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InvitationAcceptRequest {
    Long memberId;
    Long groupId;
    Long inviterId;

    @Builder
    public InvitationAcceptRequest(Long memberId, Long groupId, Long inviterId) {
        this.memberId = memberId;
        this.groupId = groupId;
        this.inviterId = inviterId;
    }

}
