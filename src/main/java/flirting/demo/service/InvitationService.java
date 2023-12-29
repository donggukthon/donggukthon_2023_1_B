package flirting.demo.service;

import flirting.demo.dto.request.InvitationAcceptRequest;
import flirting.demo.dto.response.InvitationAcceptResponse;
import flirting.demo.dto.response.InvitationResponse;
import flirting.demo.entity.Group;
import flirting.demo.entity.Invitation;
import flirting.demo.entity.Member;
import flirting.demo.exception.CommonException;
import flirting.demo.exception.ErrorCode;
import flirting.demo.repository.GroupRepository;
import flirting.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    public InvitationResponse getInvitationList(Long inviterId, Long groupId) {
        Invitation invitation = createInvitation(inviterId, groupId);
        Long memberCnt = getMemberCnt(groupId);
        InvitationResponse invitationResponse = InvitationResponse.builder()
                .invitation(invitation)
                .memberCnt(memberCnt)
                .build();

        return invitationResponse;
    }

    // Todo: groupId, memberId로 조회했을 때 두개 이상이면 예외 처리 로직 추가 -> 해결
    public InvitationAcceptResponse acceptInvitation(InvitationAcceptRequest invitationAcceptRequest) {
        Long receiverId = invitationAcceptRequest.getMemberId();
        Long groupId = invitationAcceptRequest.getGroupId();
        Long inviterId = invitationAcceptRequest.getInviterId();

        if (!memberRepository.existsById(receiverId)) {
            throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        }
        if (!memberRepository.existsById(inviterId)) {
            throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        }
        if (!groupRepository.existsById(groupId)) {
            throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        }

        Member receiver = memberRepository.getReferenceById(receiverId);
        Group group = groupRepository.getReferenceById(groupId);
        Member inviter = memberRepository.getReferenceById(inviterId);

        inviter.updateSnowflake(+20);
        memberRepository.save(inviter);

        // 예외 처리
        List<Group> alreayExistGroups = groupRepository.getGroupsByMemberId(receiverId);
        Optional<Group> duplicateGroup = alreayExistGroups.stream().filter(gr -> gr.equals(group)).findAny();
        if (duplicateGroup.isEmpty()) {

            // group에 member 추가
            receiver.getGroups().add(group);
            group.getMembers().add(receiver);

            memberRepository.save(receiver);
            groupRepository.save(group);

            InvitationAcceptResponse _invitation = InvitationAcceptResponse
                    .builder()
                    .memberName(receiver.getUsername())
                    .groupName(group.getName())
                    .build();

            return _invitation;
        } else throw new CommonException(ErrorCode.GROUP_ALREADY_JOINED);

    }


    // 이하 메소드 서비스 내부에서 호출
    private Invitation createInvitation(Long inviterId, Long groupId) {
        // Todo: 동일 member id, group id인 멤버 이미 존재하면 오류 반환 -> accept에서 처리힘
        if (!groupRepository.existsById(groupId)) {
            throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        }
        if (!memberRepository.existsById(inviterId)) {
            throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        }
        Group group = groupRepository.getReferenceById(groupId);
        Member inviter = memberRepository.getReferenceById(inviterId);

        Invitation invitation = Invitation.builder()
                .sender(inviter)
                .group(group)
                .build();

        return invitation;

    }

    private Long getMemberCnt(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        }
        return memberRepository.getMemberCnt(groupId);
    }

}
