package flirting.demo.controller;

import flirting.demo.dto.common.ResponseDto;
import flirting.demo.dto.request.VoteGuessRequest;
import flirting.demo.dto.response.VoteGuessResponse;
import flirting.demo.dto.response.VoteGuessDataResponse;
import flirting.demo.dto.request.VoteRequest;
import flirting.demo.dto.response.VoteResultResponse;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.entity.Vote;
import flirting.demo.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/vote")
public class VoteController {
    private final VoteService voteService;

    @PostMapping(value = "", produces = "application/json")
    public ResponseDto<?> save(@RequestBody VoteRequest voteRequest) {
        Vote vote = voteService.createVote(voteRequest);
        return ResponseDto.ok("투표 생성 완료");

    }

    @GetMapping(value = "/result/{memberId}/{groupId}/{questionId}")
    public ResponseDto<?> getResult(@PathVariable("memberId") Long memberId,
                                    @PathVariable("groupId") Long groupId,
                                    @PathVariable("questionId") Long questionId) {

        VoteService.VoteResult voteResult = voteService.getVoteResult(memberId, groupId, questionId);
        Question currentQuestion = voteService.getCurrentQuestion(questionId);
        Integer snowflakes = voteService.getSnowFlakes(memberId);
        Long totalVoteCnt = voteService.getTotalVoteCnt(groupId, questionId);

        VoteResultResponse voteResultResponse = VoteResultResponse.builder()
                .snowflakes(snowflakes)
                .totalVoteCnt(totalVoteCnt)
                .question(currentQuestion)
                .voteResult(voteResult)
                .build();

        return ResponseDto.ok(voteResultResponse);
    }

    @GetMapping(value = "/guess/{memberId}/{groupId}/{questionId}", produces = "application/json")
    public ResponseDto<?> getGuessData(@PathVariable("memberId") Long memberId,
                                       @PathVariable("groupId") Long groupId,
                                       @PathVariable("questionId") Long questionId) {
        Question question = voteService.getCurrentQuestion(questionId);
        List<Member> options = voteService.getOptionList(memberId, groupId);
        Integer snowflakes = voteService.getSnowFlakes(memberId);
        Long memberCnt = voteService.getMemberCnt(groupId);

        VoteGuessDataResponse voteGuessDataResponse = VoteGuessDataResponse.builder()
                .snowflakes(snowflakes)
                .question(question)
                .memberCnt(memberCnt)
                .members(options)
                .build();


        return ResponseDto.ok(voteGuessDataResponse);
    }

    @PutMapping(value = "/guess", produces = "application/json")
    public ResponseDto<?> guess(@RequestBody VoteGuessRequest voteGuessRequest) {
        Long memberId = voteGuessRequest.getMemberId();
        Long selecteMemberId = voteGuessRequest.getSelectedMemberId();
        boolean isCorrect = voteService.getIsCorrect(voteGuessRequest);

        Member _member = voteService.updateSnowFlakes(memberId);
        Member selectedMember = voteService.getMemberById(selecteMemberId);

        VoteGuessResponse voteGuessResponse = VoteGuessResponse.builder()
                .member(_member)
                .selectedMember(selectedMember)
                .isCorrect(isCorrect)
                .snowflakes(_member.getSnowflake())
                .build();

        return ResponseDto.ok(voteGuessResponse);
    }
}
