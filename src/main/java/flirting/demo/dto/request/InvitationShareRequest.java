package flirting.demo.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InvitationShareRequest {
    Long memberId;

    public InvitationShareRequest(Long memberId) {
        this.memberId = memberId;
    }
}