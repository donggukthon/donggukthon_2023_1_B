package flirting.demo.service;

import flirting.demo.dto.request.GroupCreateRequest;
import flirting.demo.entity.Group;
import flirting.demo.entity.Member;
import flirting.demo.repository.GroupRepository;
import flirting.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    public Group create(GroupCreateRequest groupCreateRequest) {
            Group group = groupRepository.save(
                    Group.builder()
                        .name(groupCreateRequest.getName())
                        .build()
            );
            Member member = memberRepository.getReferenceById(groupCreateRequest.getMemberId());
            group.getMembers().add(member);
            member.getGroups().add(group);
            groupRepository.save(group);
            memberRepository.save(member);
            return group;
    }

    public List<Group> getGroups(Long memberId) {
            List<Group> groups = groupRepository.getGroupsByMemberId(memberId);
            return groups;

    }
}
