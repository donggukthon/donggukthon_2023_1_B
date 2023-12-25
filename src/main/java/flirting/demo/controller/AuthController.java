package flirting.demo.controller;

import flirting.demo.dto.common.ResponseDto;
import flirting.demo.dto.response.MemberInfoResponse;
import flirting.demo.dto.response.MemberResponse;
import flirting.demo.entity.Member;
import flirting.demo.exception.CommonException;
import flirting.demo.exception.ErrorCode;
import flirting.demo.service.OAuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final OAuthService oAuthService;

    @GetMapping("/callback/google")
    public RedirectView successGoogleLogin(@RequestParam("code") String accessCode) {
        String userToken = oAuthService.getGoogleAccessToken(accessCode);
        String redirectUrl = "https://flirting.vercel.app/googleLogin?token=" + userToken;
        return new RedirectView(redirectUrl);
    }

    @GetMapping("/login")
    public RedirectView redirectToGoogle() {
        return oAuthService.redirectToGoogle();
    }

    @GetMapping("/decode")
    public ResponseDto<?> jwtDecode(@RequestHeader(value = "Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring("Bearer ".length());
            MemberResponse memberInfo = oAuthService.decodeToken(token);
            Member member = oAuthService.findMemberByOauthId(memberInfo.getOauthId());

            MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
            memberInfoResponse.setMemberResponse(memberInfo);
            memberInfoResponse.setMemberId(member.getId());
            memberInfoResponse.setSnowflake(member.getSnowflake());

            return ResponseDto.ok("유저 조회 성공");

        } catch (Exception e) {
            throw new CommonException(ErrorCode.TOKEN_DECODE_FAILED);
        }
    }
}
