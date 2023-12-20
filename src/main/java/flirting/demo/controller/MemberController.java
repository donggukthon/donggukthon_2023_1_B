package flirting.demo.controller;

import flirting.demo.common.ApiStatus;
import flirting.demo.common.ResponseData;
import flirting.demo.common.StatusCode;
import flirting.demo.dto.response.SettingResponse;
import flirting.demo.entity.Member;
import flirting.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.ok("eroom 배포 자동화 테스트");
    }

    @GetMapping("/health")
    private ResponseEntity<ApiStatus> healthCheck() {
        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(
                new ApiStatus(StatusCode.OK, "요청 확인"),
                httpHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/setting/{memberId}", produces = "application/json")
    private ResponseEntity<Object> getSetting(@PathVariable("memberId") Long memberId){
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            Member member = memberService.getMemberById(memberId);
            SettingResponse settingResponse = SettingResponse.builder()
                    .member(member)
                    .build();

            return new ResponseEntity<>(
                    new ResponseData(
                            new ApiStatus(StatusCode.OK, "설정 정보 조회"),
                            settingResponse
                    ), httpHeaders, HttpStatus.OK
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage()),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
