package flirting.demo.service;

import flirting.demo.entity.Member;
import flirting.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member getMemberById(Long memberId) {
        return memberRepository.getReferenceById(memberId);
    }
}
