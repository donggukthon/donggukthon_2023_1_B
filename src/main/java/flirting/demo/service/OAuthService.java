package flirting.demo.service;

import flirting.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class OAuthService {
    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    @Value("${oauth2.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${oauth2.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${oauth2.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URL;
    @Value("${oauth2.google.scope}")
    private String scope;

    private String baseUrl = "https://accounts.google.com/o/oauth2/v2/auth";

    @Autowired
    private final MemberRepository memberRepository;

    public RedirectView redirectToGoogle() {
        String url = baseUrl + "?client_id=" + GOOGLE_CLIENT_ID +
                "&response_type=code" +
                "&redirect_uri=" + GOOGLE_REDIRECT_URL +
                "&scope=" + scope;
        return new RedirectView(url);
    }

    public ResponseEntity<String> getGoogleAccessToken(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        System.out.println(GOOGLE_REDIRECT_URL);

        params.put("code", accessToken);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_REDIRECT_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }

        return null;
    }

}
