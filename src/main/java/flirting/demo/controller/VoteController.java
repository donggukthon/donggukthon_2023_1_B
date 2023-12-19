package flirting.demo.controller;

import flirting.demo.common.ApiStatus;
import flirting.demo.common.ResponseData;
import flirting.demo.common.StatusCode;
import flirting.demo.dto.QuestionDataResponse;
import flirting.demo.dto.VoteGuessDataResponse;
import flirting.demo.dto.VoteRequest;
import flirting.demo.dto.VoteResultResponse;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.entity.Vote;
import flirting.demo.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/vote")
public class VoteController {
    private final VoteService voteService;

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<ApiStatus> save(@RequestBody VoteRequest voteRequest){
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            Vote vote = voteService.createVote(voteRequest);
            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.OK, "투표 생성 완료"),
                            httpHeaders, HttpStatus.OK);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.INTERNAL_SERVER_ERROR, "투표 생성 실패"),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/result/{memberId}/{groupId}/{questionId}")
    public ResponseEntity<Object> getResult(@PathVariable("memberId") Long memberId,
                                            @PathVariable("groupId") Long groupId,
                                            @PathVariable("questionId") Long questionId){
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
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

            return new ResponseEntity<>(
                    new ResponseData(
                            new ApiStatus(StatusCode.OK, "투표 결과 조회 성공"),
                            voteResultResponse
                    ),
                    httpHeaders, HttpStatus.OK
            );
        }catch (RuntimeException e) {
            // Todo: CustomException 반환하는 걸로 수정
            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage()),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/guess/{memberId}/{groupId}/{questionId}", produces = "application/json")
    public ResponseEntity<Object> getGuessData(@PathVariable("memberId") Long memberId,
                                               @PathVariable("groupId") Long groupId,
                                               @PathVariable("questionId") Long questionId){
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
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


            return new ResponseEntity<>(
                    new ResponseData(
                            new ApiStatus(StatusCode.OK, "투표 맞추기 화면 조회 성공"),
                            voteGuessDataResponse
                    ),
                    httpHeaders, HttpStatus.OK
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage()),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
