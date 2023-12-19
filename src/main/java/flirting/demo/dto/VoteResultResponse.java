package flirting.demo.dto;

import flirting.demo.entity.Question;
import flirting.demo.service.VoteService;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteResultResponse {
    Long questionId;
    String question;
    String mostVoted;
    Integer mostVotedCnt;
    String memberName;
    Integer myVoteCnt;

    @Builder
    public VoteResultResponse(Question question, VoteService.VoteResult voteResult) {
        this.questionId = question.getId();
        this.question = question.getContent();
        this.mostVoted = voteResult.getMostVoted();
        this.mostVotedCnt = voteResult.getMostVotedCnt();
        this.memberName = voteResult.getMemberName();
        this.myVoteCnt = voteResult.getMyVoteCnt();
    }
}
