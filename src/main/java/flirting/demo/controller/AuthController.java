package flirting.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import flirting.demo.dto.response.MemberResponse;
import flirting.demo.service.OAuthService;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<?> jwtDecode(@RequestParam("token") String token) {
        try {
            System.out.println("token = " + token);
            MemberResponse memberInfo = oAuthService.decodeToken(token);
            return ResponseEntity.ok(memberInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token decoding failed");
        }    }
}
