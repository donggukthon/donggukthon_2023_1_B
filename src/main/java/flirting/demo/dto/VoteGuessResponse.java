package flirting.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import flirting.demo.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteGuessResponse {
    @JsonProperty("isCorrect")
    boolean isCorrect;
    String member;
    String selectedMember;
    Integer snowflakes;

    @JsonIgnore
    public boolean isIsCorrect() {
        return this.isCorrect;
    }

    @Builder
    public VoteGuessResponse(boolean isCorrect, Member member, Member selectedMember, Integer snowflakes) {
        this.isCorrect = isCorrect;
        this.member = member.getUsername();
        this.selectedMember = selectedMember.getUsername();
        this.snowflakes = snowflakes;
    }
}
