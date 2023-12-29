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
        return ResponseDto.ok(voteService.getTotalResult(memberId, groupId, questionId));
    }

    @GetMapping(value = "/guess/{memberId}/{groupId}/{questionId}", produces = "application/json")
    public ResponseDto<?> getGuessData(@PathVariable("memberId") Long memberId,
                                       @PathVariable("groupId") Long groupId,
                                       @PathVariable("questionId") Long questionId) {
        return ResponseDto.ok(voteService.getGuessData(memberId, groupId, questionId));
    }

    @PutMapping(value = "/guess", produces = "application/json")
    public ResponseDto<?> guess(@RequestBody VoteGuessRequest voteGuessRequest) {
        return ResponseDto.ok(voteService.getGuessResult(voteGuessRequest));
    }
}
