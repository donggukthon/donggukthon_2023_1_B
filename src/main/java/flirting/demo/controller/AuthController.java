package flirting.demo.controller;

import flirting.demo.dto.common.ApiStatus;
import flirting.demo.dto.common.ResponseData;
import flirting.demo.dto.common.StatusCode;
import flirting.demo.dto.response.MemberInfoResponse;
import flirting.demo.dto.response.MemberResponse;
import flirting.demo.entity.Member;
import flirting.demo.service.OAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        String redirectUrl = "http://localhost:3000/googleLogin?token=" + userToken;
        return new RedirectView(redirectUrl);
    }

    @GetMapping("/login")
    public RedirectView redirectToGoogle() {
        return oAuthService.redirectToGoogle();
    }

    @GetMapping("/decode")
    public ResponseEntity<?> jwtDecode(@RequestHeader(value = "Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring("Bearer ".length());
            HttpHeaders httpHeaders = new HttpHeaders();
            MemberResponse memberInfo = oAuthService.decodeToken(token);
            Member member = oAuthService.findMemberByOauthId(memberInfo.getOauthId());

            MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
            memberInfoResponse.setMemberResponse(memberInfo);
            memberInfoResponse.setMemberId(member.getId());
            memberInfoResponse.setSnowflake(member.getSnowflake());

            return new ResponseEntity<>(new ResponseData(
                    new ApiStatus(StatusCode.OK, "유저 조회 성공"),
                    memberInfoResponse
            ), httpHeaders, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token decoding failed");
        }
    }
}
