package flirting.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import flirting.demo.entity.Member;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberResponse {

    @JsonProperty("sub")
    private String oauthId;
    @JsonProperty("name")
    private String username;
    private String email;

    @Builder
    public MemberResponse(String oauthId, String username, String email) {

        this.oauthId = oauthId;
        this.username = username;
        this.email = email;
    }

    public Member toEntity() {
        return Member.builder()
                .oauthId(oauthId)
                .username(username)
                .email(email)
                .build();
    }
}
