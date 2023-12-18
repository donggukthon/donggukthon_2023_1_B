package flirting.demo.controller;

import flirting.demo.common.ApiStatus;
import flirting.demo.common.StatusCode;
import flirting.demo.dto.VoteRequest;
import flirting.demo.entity.Vote;
import flirting.demo.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping(value = "/vote", produces = "application/json")
    public ResponseEntity<ApiStatus> save(@RequestBody VoteRequest voteRequest){
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            Vote vote = voteService.createVote(voteRequest);
            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.OK, "투표 생성 완료"),
                            httpHeaders, HttpStatus.OK);

        }catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.INTERNAL_SERVER_ERROR, "투표 생성 실패;"),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
