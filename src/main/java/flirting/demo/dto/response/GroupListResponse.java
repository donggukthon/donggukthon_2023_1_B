package flirting.demo.dto.response;

import flirting.demo.entity.Group;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GroupListResponse {

    List<GroupListData> groupList = new ArrayList<>();

    public GroupListResponse(List<Group> groups) {
        groups.forEach(group -> {
            this.groupList.add(GroupListData.builder()
                            .groupId(group.getId())
                            .groupName(group.getName())
                    .build());
        });
    }

    @Getter
    public static class GroupListData {
        Long groupId;
        String groupName;

        @Builder
        public GroupListData (Long groupId, String groupName) {
            this.groupId = groupId;
            this.groupName = groupName;
        }
    }
}
