package flirting.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import flirting.demo.entity.Invitation;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InvitationListResponse {
    private List<InvitationData> invitationList = new ArrayList<>();

    @Builder
    public InvitationListResponse(List<Invitation> invitations) {
        invitations.forEach(invitation -> {
            this.invitationList.add(InvitationData.builder()
                            .inviterId(invitation.getId())
                            .inviterName(invitation.getSender().getUsername())
                            .groupId(invitation.getGroup().getId())
                            .groupName(invitation.getGroup().getName())
                    .build());
        });
    }

    @Getter
    public static class InvitationData {
        @JsonProperty("inviterId")
        private Long inviterId;

        @JsonProperty("inviterName")
        private String inviterName;

        @JsonProperty("groupId")
        private Long groupId;

        @JsonProperty("groupName")
        private String groupName;

        @Builder
        public InvitationData(Long inviterId, String inviterName, Long groupId, String groupName){
            this.inviterId = inviterId;
            this.inviterName = inviterName;
            this.groupId = groupId;
            this.groupName = groupName;
        }


    }
}
