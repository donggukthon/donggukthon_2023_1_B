package flirting.demo.controller;

import flirting.demo.common.ApiStatus;
import flirting.demo.common.CustomException;
import flirting.demo.common.ResponseData;
import flirting.demo.common.StatusCode;
import flirting.demo.dto.QuestionDataResponse;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.repository.QuestionRepository;
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
    // Todo: 사용해도 되는 패턴인지 조사 -> 안됨. 비동기 실행되는 듯. 왜그런지 조사

    @GetMapping(value = "/{memberId}/{groupId}/{questionId}", produces = "application/json")
    public ResponseEntity<Object> getQuestionList(@PathVariable Long memberId,
                                                  @PathVariable Long groupId,
                                                  @PathVariable Long questionId) {
        HttpHeaders httpHeaders = new HttpHeaders();

        List<Long> questionIdList = questionService.getQuestionIdList();
        Long _questionId = questionId;
        if (_questionId < 0) { // 첫 투표 질문 조회인 경우
            Optional<Long> opQuestion = questionIdList.stream().findFirst();
            if (opQuestion.isEmpty()) {
                return new ResponseEntity<>(
                        new ApiStatus(StatusCode.NO_SELECTED_QUESTION, "조회된 질문이 없습니다."),
                        httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
            _questionId = opQuestion.get();
        }
        try {
            Question question = questionService.getCurrentQuestion(_questionId);
            List<Member> options = questionService.getOptionList(memberId, groupId);

            QuestionDataResponse questionDataResponse = QuestionDataResponse.builder()
                    .questionIdList(questionIdList)
                    .question(question)
                    .members(options)
                    .build();


            return new ResponseEntity<>(
                    new ResponseData(
                            new ApiStatus(StatusCode.OK, "투표 질문 조회 성공"),
                            questionDataResponse
                    ),
                    httpHeaders, HttpStatus.OK
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.INTERNAL_SERVER_ERROR, "투표 질문 조회 실패"),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
