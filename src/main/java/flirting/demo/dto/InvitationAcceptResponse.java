package flirting.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InvitationAcceptResponse {
    String memberName;
    String groupName;

    @Builder
    public InvitationAcceptResponse(String memberName, String groupName) {
        this.memberName = memberName;
        this.groupName = groupName;
    }
}
