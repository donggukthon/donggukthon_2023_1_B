package flirting.demo.service;

import flirting.demo.dto.request.GroupCreateRequest;
import flirting.demo.dto.response.GroupCreateResponse;
import flirting.demo.dto.response.GroupListResponse;
import flirting.demo.entity.Group;
import flirting.demo.entity.Member;
import flirting.demo.exception.CommonException;
import flirting.demo.exception.ErrorCode;
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

    public GroupCreateResponse create(GroupCreateRequest groupCreateRequest) {
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
            return new GroupCreateResponse(group.getId());
    }

    public GroupListResponse getGroups(Long memberId) {
        // memberId에 해당하는 멤버가 없는 경우 예외처리
        if (!memberRepository.existsById(memberId)){
            throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        List<Group> groups = groupRepository.getGroupsByMemberId(memberId);
        return new GroupListResponse(groups);
    }
}
