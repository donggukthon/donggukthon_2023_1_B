package flirting.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import flirting.demo.entity.Group;
import flirting.demo.entity.Invitation;
import flirting.demo.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InvitationResponse {
    private Long inviterId;
    private String inviterName;
    Long groupId;
    String groupName;
    Long memberCnt;

    @Builder
    public InvitationResponse(Invitation invitation, Long memberCnt) {
        this.inviterId = invitation.getSender().getId();
        this.inviterName = invitation.getSender().getUsername();
        this.groupId = invitation.getGroup().getId();
        this.groupName = invitation.getGroup().getName();
        this.memberCnt = memberCnt;
    }

}
