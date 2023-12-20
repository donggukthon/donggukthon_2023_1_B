package flirting.demo.service;

import flirting.demo.dto.request.InvitationAcceptRequest;
import flirting.demo.dto.response.InvitationAcceptResponse;
import flirting.demo.entity.Group;
import flirting.demo.entity.Invitation;
import flirting.demo.entity.Member;
import flirting.demo.repository.GroupRepository;
import flirting.demo.repository.InvitationRepository;
import flirting.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final MemberRepository memberRepository;
    private final InvitationRepository invitationRepository;
    private final GroupRepository groupRepository;

    public Invitation createInvitation(Long inviterId, Long groupId) {
            // Todo: get memer id -> invitation 생성 후 저장 ...
            // Todo: 동일 member id, group id인 멤버 이미 존재하면 오류 반환
            Group group = groupRepository.getReferenceById(groupId);
            Member inviter = memberRepository.getReferenceById(inviterId);
            System.out.println("get group and inviter");
            Invitation invitation = Invitation.builder()
                    .sender(inviter)
                    .group(group)
                    .build();
            System.out.println("build invitation");
//            Invitation _invitation = invitationRepository.save(invitation);
            System.out.println("save invitation");
            return invitation;

    }
    public Long getMemberCnt(Long groupId) {
        return  memberRepository.getMemberCnt(groupId);
    }

    public InvitationAcceptResponse acceptInvitation(InvitationAcceptRequest invitationAcceptRequest) {
            Long receiverId = invitationAcceptRequest.getMemberId();
            Long groupId = invitationAcceptRequest.getGroupId();
            Long inviterId = invitationAcceptRequest.getInviterId();
            // Todo: groupId, memberId로 조회했을 때 두개 이상이면 예외 처리 로직 추갸
//            Invitation invitation = invitationRepository.getInvitationByReceiverAndGroup(receiverId, groupId)
//                    .orElseThrow(() -> new RuntimeException("조건에 맞는 초대장이 없습니다."));

            // 초대장 수락 상태로 변경
//            if (invitation.isAccepted() == false) {
//                invitation.updateIsAccepted(true);
//            }

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
            }else throw new RuntimeException("이미 가입된 그룹입니다.");
            // Todo: custom runtime exception 만들기

    }
}
