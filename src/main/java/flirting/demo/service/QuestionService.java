package flirting.demo.service;

import flirting.demo.dto.response.QuestionDataResponse;
import flirting.demo.dto.response.QuestionListResponse;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.exception.CommonException;
import flirting.demo.exception.ErrorCode;
import flirting.demo.repository.GroupRepository;
import flirting.demo.repository.MemberRepository;
import flirting.demo.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    public QuestionDataResponse getQuestion(Long memberId, Long groupId, Long questionId) {
        List<Long> questionIdList = getQuestionIdList();
        Long _questionId = questionId;
        if (_questionId < 0) { // 첫 투표 질문 조회인 경우
            Optional<Long> opQuestion = questionIdList.stream().findFirst();
            if (opQuestion.isEmpty()) {
                throw new CommonException(ErrorCode.QUESTION_NOT_FOUND);
            }
            _questionId = opQuestion.get();
        }
        Question question = getCurrentQuestion(_questionId);
        List<Member> options = getOptionList(memberId, groupId);
        Long memberCnt = getMemberCnt(groupId);

        QuestionDataResponse questionDataResponse = QuestionDataResponse.builder()
                .questionIdList(questionIdList)
                .question(question)
                .members(options)
                .memberCnt(memberCnt)
                .build();

        return questionDataResponse;
    }


    public QuestionListResponse getQuestionList() {
        List<Question> questions = questionRepository.getSelectedQuestion();
        if (questions.size() != 7) {
            throw new CommonException(ErrorCode.QUESTION_CNT_NOT_SEVEN);
        }

        QuestionListResponse questionListResponse = new QuestionListResponse(questions);
        return questionListResponse;
    }



    private List<Long> getQuestionIdList() {
        List<Question> questions = questionRepository.getSelectedQuestion();
        if (questions.size() != 7) {
            throw new CommonException(ErrorCode.QUESTION_CNT_NOT_SEVEN);
        }
        List<Long> questionIdList = questions.stream()
                .map(question -> question.getId()).toList();
        return questionIdList;

    }

    private List<Member> getOptionList(Long memberId, Long groupId) {
        if (!memberRepository.existsById(memberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        List<Member> options = memberRepository.getGroupMembersExceptMe(memberId, groupId);
        return options;
    }

    private Question getCurrentQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) throw new CommonException(ErrorCode.QUESTION_NOT_FOUND);
        return questionRepository.getReferenceById(questionId);
    }

    private Long getMemberCnt(Long groupId) {
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        return memberRepository.getMemberCnt(groupId);
    }
}
