package flirting.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import flirting.demo.dto.response.GoogleOAuthResponse;
import flirting.demo.dto.response.MemberResponse;
import flirting.demo.entity.Member;
import flirting.demo.repository.MemberRepository;
import io.jsonwebtoken.impl.Base64UrlCodec;
import io.jsonwebtoken.io.Decoders;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;


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

    public MemberResponse decodeToken(String token) {
        String decode = decryptBase64UrlToken(token.split("\\.")[1]);
        System.out.println("decode = " + decode);
        return transJsonToMemberInfoDto(decode);
    }

    public String decryptBase64UrlToken(String jwtToken) {
        byte[] decode = Decoders.BASE64URL.decode(jwtToken);
        return new String(decode, StandardCharsets.UTF_8);
    }

    public MemberResponse transJsonToMemberInfoDto(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            MemberResponse dto = mapper.readValue(json, MemberResponse.class);
            System.out.println("MemberRespone로 변경= " + dto);
            saveMemberInfo(dto);
            return dto;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void saveMemberInfo(MemberResponse dto) {
        try {
            Optional<Member> member = memberRepository.findByOauthId(dto.getOauthId());
            System.out.println("멤버 조회");
            System.out.println("OAuth ID: " + dto.getOauthId());
            System.out.println("member 존재 여부: " + member.isPresent());
            processMember(member, dto);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 정보 출력
            // 필요한 경우, 여기서 추가적인 예외 처리를 할 수 있습니다.
        }
    }

    private void processMember(Optional<Member> member, MemberResponse dto) {
        member.ifPresentOrElse(
                existingMember -> {
                    System.out.println("멤버가 이미 존재합니다: " + existingMember);
                },
                () -> {
                    Member newMember = dto.toEntity();
                    System.out.println("새 멤버 저장됨: " + newMember);
                    memberRepository.save(newMember);
                }
        );
    }
    public Member findMemberByOauthId(String oauthId) {
        return memberRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }
}
