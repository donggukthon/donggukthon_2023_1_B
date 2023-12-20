package flirting.demo.dto.response;

import flirting.demo.entity.Question;
import flirting.demo.service.VoteService;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteResultResponse {
    Integer snowflakes;
    Long questionId;
    String question;
    String mostVoted;
    Integer mostVotedCnt;
    String memberName;
    Integer myVoteCnt;

    @Builder
    public VoteResultResponse(Integer snowflakes, Question question, VoteService.VoteResult voteResult) {
        this.snowflakes = snowflakes;
        this.questionId = question.getId();
        this.question = question.getContent();
        this.mostVoted = voteResult.getMostVoted();
        this.mostVotedCnt = voteResult.getMostVotedCnt();
        this.memberName = voteResult.getMemberName();
        this.myVoteCnt = voteResult.getMyVoteCnt();
    }
}
