package flirting.demo.dto.request;

import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.entity.Vote;
import lombok.Data;
import lombok.Getter;

@Getter
public class VoteRequest {
    Long questionId;
    Long voterId;
    Long selectedMemberId;

    public Vote toEntity(Question question, Member voter, Member selectedMember){
        return Vote.builder()
                .question(question)
                .voter(voter)
                .selectedMember(selectedMember)
                .build();
    }
}
