package flirting.demo.dto.response;

import lombok.Getter;

@Getter
public class GroupCreateResponse {
    Long groupId;

    public GroupCreateResponse(Long groupId){
        this.groupId = groupId;
    }
}
