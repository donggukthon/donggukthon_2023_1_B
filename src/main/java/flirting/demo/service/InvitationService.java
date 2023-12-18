package flirting.demo.service;

import flirting.demo.dto.InvitationAcceptRequest;
import flirting.demo.dto.InvitationShareRequest;
import flirting.demo.entity.Group;
import flirting.demo.entity.Invitation;
import flirting.demo.entity.Member;
import flirting.demo.repository.GroupRepository;
import flirting.demo.repository.InvitationRepository;
import flirting.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final MemberRepository memberRepository;
    private final InvitationRepository invitationRepository;
    private final GroupRepository groupRepository;

    public Member shareInvitation(InvitationShareRequest invitationShareRequest){
        try {
            Long memberId = invitationShareRequest.getMemberId();
            Member member = memberRepository.getReferenceById(memberId);
            member.updateSnowflake(+20);

            Member _member = memberRepository.save(member);
            return _member;
        }
        catch (RuntimeException e) {
            throw new RuntimeException("눈송이 증정 실패");
        }
    }

    public Invitation acceptInvitation(InvitationAcceptRequest invitationAcceptRequest) {
        try {
            Long receiverId = invitationAcceptRequest.getMemberId();
            Long groupId = invitationAcceptRequest.getGroupId();
            // Todo: groupId, memberId로 조회했을 때 두개 이상이면 예외 처리 로직 추갸
            Invitation invitation = invitationRepository.getInvitationByReceiverAndGroup(receiverId, groupId)
                    .orElseThrow(() -> new RuntimeException("조건에 맞는 초대장이 없습니다."));

            // 초대장 수락 상태로 변경
            if (invitation.isAccepted() == false) {
                invitation.updateIsAccepted(true);
            }

            Member member = memberRepository.getReferenceById(receiverId);
            Group group = groupRepository.getReferenceById(groupId);

            // group에 member 추가
            member.getGroups().add(group);
            group.getMembers().add(member);

            memberRepository.save(member);
            groupRepository.save(group);

            Invitation _invitation = invitationRepository.save(invitation);

            return _invitation;
        }
        catch (RuntimeException e) {
            throw new RuntimeException("초대 수락 실패");
        }
    }
}
