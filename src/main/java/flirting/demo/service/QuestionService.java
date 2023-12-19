package flirting.demo.service;

import flirting.demo.common.CustomException;
import flirting.demo.common.StatusCode;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.repository.MemberRepository;
import flirting.demo.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    public List<Long> getQuestionIdList() {
        List<Question> questions = questionRepository.getSelectedQuestion();
        if (questions.size() != 7) {
            throw new CustomException(StatusCode.QUESTION_CNT_NOT_SEVEN);
        }
        try{
            List<Long> questionIdList = questions.stream()
                    .map(question -> question.getId()).toList();
            return questionIdList;
        }catch (RuntimeException e) {
            throw new RuntimeException("질문의 id를 조회하는 과정에서 에러 발생");
        }
    }

    public List<Member> getOptionList(Long memberId, Long groupId) {

        List<Member> options = memberRepository.getGroupMembersExceptMe(memberId, groupId);
        // 그룹에 멤버가 나 혼자
        if (options.size() == 0) {
            throw new CustomException(StatusCode.NO_OTHER_MEMBERS_IN_GROUP);
        }
        // 자기 자신 제외하고 주기
        else if (options.stream().filter(op -> op.getId() == memberId).findAny().isPresent()) {
            throw new CustomException(StatusCode.MYSELF_IN_OPTIONS);
        }
        return options;
    }

    public Question getCurrentQuestion(Long questionId) {
        return questionRepository.getReferenceById(questionId);
    }

    public List<Question> getQuestionList() {
        List<Question> questions = questionRepository.getSelectedQuestion();
        if (questions.size() != 7) {
            throw new CustomException(StatusCode.QUESTION_CNT_NOT_SEVEN);
        }
        return questions;
    }
}
