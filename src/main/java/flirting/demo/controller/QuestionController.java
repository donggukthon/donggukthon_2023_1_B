package flirting.demo.controller;

import flirting.demo.dto.common.ApiStatus;
import flirting.demo.dto.common.ResponseData;
import flirting.demo.dto.common.ResponseDto;
import flirting.demo.dto.common.StatusCode;
import flirting.demo.dto.response.QuestionDataResponse;
import flirting.demo.dto.response.QuestionListResponse;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.exception.CommonException;
import flirting.demo.exception.ErrorCode;
import flirting.demo.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        List<Long> questionIdList = questionService.getQuestionIdList();
        Long _questionId = questionId;
        if (_questionId < 0) { // 첫 투표 질문 조회인 경우
            Optional<Long> opQuestion = questionIdList.stream().findFirst();
            if (opQuestion.isEmpty()) {
                throw new CommonException(ErrorCode.QUESTION_NOT_FOUND);
            }
            _questionId = opQuestion.get();
        }
        Question question = questionService.getCurrentQuestion(_questionId);
        List<Member> options = questionService.getOptionList(memberId, groupId);
        Long memberCnt = questionService.getMemberCnt(groupId);

        QuestionDataResponse questionDataResponse = QuestionDataResponse.builder()
                .questionIdList(questionIdList)
                .question(question)
                .members(options)
                .memberCnt(memberCnt)
                .build();


        return ResponseDto.ok(questionDataResponse);

    }

    @GetMapping(value = "", produces = "application/json")
    public ResponseDto<?> getQuestionList() {
        List<Question> questions = questionService.getQuestionList();
        QuestionListResponse questionListResponse = new QuestionListResponse(questions);
        return ResponseDto.ok(questionListResponse);
    }
}
