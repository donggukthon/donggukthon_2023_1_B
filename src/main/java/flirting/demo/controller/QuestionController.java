package flirting.demo.controller;

import flirting.demo.dto.common.ResponseDto;
import flirting.demo.dto.response.QuestionDataResponse;
import flirting.demo.dto.response.QuestionListResponse;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.exception.CommonException;
import flirting.demo.exception.ErrorCode;
import flirting.demo.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/question")
public class QuestionController {
    private final QuestionService questionService;

    // Todo: 사용해도 되는 패턴인지 조사 -> 안됨. 비동기 실행되는 듯. 왜그런지 조사 (repository - service)
    @GetMapping(value = "/{memberId}/{groupId}/{questionId}", produces = "application/json")
    public ResponseDto<?> getQuestion(@PathVariable Long memberId,
                                      @PathVariable Long groupId,
                                      @PathVariable Long questionId) {
        return ResponseDto.ok(questionService.getQuestion(memberId, groupId, questionId));
    }

    @GetMapping(value = "", produces = "application/json")
    public ResponseDto<?> getQuestionList() {
        return ResponseDto.ok(questionService.getQuestionList());
    }
}
