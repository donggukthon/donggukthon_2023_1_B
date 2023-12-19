package flirting.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class VoteGuessDataResponse {

    Integer snowflakes;
    Long questionId;
    String question;
    Long memberCnt;
    List<OptionData> optionList;

    @Builder
    public VoteGuessDataResponse(Integer snowflakes, Question question, List<Member> members, Long memberCnt) {
//        System.out.println("question: "+ question.getId() + " " + question.getContent());
        this.snowflakes = snowflakes;
        this.questionId = question.getId();
        this.question = question.getContent();
        this.memberCnt = memberCnt;
        this.optionList = members.stream().map(member -> OptionData.builder().member(member).build()).toList();
    }

    @Getter
    public static class OptionData {
        @JsonProperty("memberId")
        Long memberId;
        @JsonProperty("memberName")
        String memberName;

        @Builder
        public OptionData(Member member) {
            this.memberId = member.getId();
            this.memberName = member.getUsername();
        }
    }

}
