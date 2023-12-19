package flirting.demo.dto;

import flirting.demo.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SettingResponse {
    String name;
    Integer snowflakes;
    String email;

    @Builder
    public SettingResponse(Member member) {
        this.name = member.getUsername();
        this.snowflakes = member.getSnowflake();
        this.email = member.getEmail();
    }
}
