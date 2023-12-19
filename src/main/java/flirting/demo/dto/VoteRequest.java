package flirting.demo.dto;

import flirting.demo.entity.Group;
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
    Long groupId;

    public Vote toEntity(Question question, Member voter, Member selectedMember, Group group){

        return Vote.builder()
                .question(question)
                .voter(voter)
                .selectedMember(selectedMember)
                .group(group)
                .build();
    }
}
