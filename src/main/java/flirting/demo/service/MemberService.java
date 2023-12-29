package flirting.demo.service;

import flirting.demo.dto.response.SettingResponse;
import flirting.demo.entity.Member;
import flirting.demo.exception.CommonException;
import flirting.demo.exception.ErrorCode;
import flirting.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public SettingResponse getSetting(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        }
        Member member = memberRepository.getReferenceById(memberId);
        SettingResponse settingResponse = SettingResponse.builder()
                .member(member)
                .build();
        return settingResponse;
    }
}
