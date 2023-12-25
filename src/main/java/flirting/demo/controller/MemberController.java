package flirting.demo.controller;

import flirting.demo.dto.common.ResponseDto;
import flirting.demo.dto.response.SettingResponse;
import flirting.demo.entity.Member;
import flirting.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/")
    public ResponseEntity hello() {
        return ResponseEntity.ok("배포 자동화");
    }

    @GetMapping(value = "/setting/{memberId}", produces = "application/json")
    private ResponseDto<?> getSetting(@PathVariable("memberId") Long memberId) {
        Member member = memberService.getMemberById(memberId);
        SettingResponse settingResponse = SettingResponse.builder()
                .member(member)
                .build();

        return ResponseDto.ok(settingResponse);

    }

}
