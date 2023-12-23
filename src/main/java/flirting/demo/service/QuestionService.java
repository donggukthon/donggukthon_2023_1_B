package flirting.demo.service;

import flirting.demo.dto.common.CustomException;
import flirting.demo.dto.common.StatusCode;
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
            List<Long> questionIdList = questions.stream()
                    .map(question -> question.getId()).toList();
            return questionIdList;

    }

    public List<Member> getOptionList(Long memberId, Long groupId) {

        List<Member> options = memberRepository.getGroupMembersExceptMe(memberId, groupId);
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

    public Long getMemberCnt(Long groupId) {
        return memberRepository.getMemberCnt(groupId);
    }
}
