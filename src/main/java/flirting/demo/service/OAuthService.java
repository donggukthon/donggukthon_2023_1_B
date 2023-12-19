package flirting.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import flirting.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    public String getGoogleAccessToken(String accessCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", accessCode);
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_CLIENT_SECRET);
        params.add("redirect_uri", GOOGLE_REDIRECT_URL);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(GOOGLE_TOKEN_URL, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode rootNode = mapper.readTree(response.getBody());
                String idToken = rootNode.path("id_token").asText();
                return idToken; // id_token 반환
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
