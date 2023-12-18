package flirting.demo.service;

import flirting.demo.dto.InvitationShareRequest;
import flirting.demo.entity.Member;
import flirting.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final MemberRepository memberRepository;
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
}
