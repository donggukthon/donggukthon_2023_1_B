package flirting.demo.service;

import flirting.demo.dto.common.CustomException;
import flirting.demo.dto.common.StatusCode;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.exception.CommonException;
import flirting.demo.exception.ErrorCode;
import flirting.demo.repository.GroupRepository;
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
    private final GroupRepository groupRepository;

    public List<Long> getQuestionIdList() {
        List<Question> questions = questionRepository.getSelectedQuestion();
        if (questions.size() != 7) {
            throw new CommonException(ErrorCode.QUESTION_CNT_NOT_SEVEN);
        }
        List<Long> questionIdList = questions.stream()
                .map(question -> question.getId()).toList();
        return questionIdList;

    }

    public List<Member> getOptionList(Long memberId, Long groupId) {
        if (!memberRepository.existsById(memberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        List<Member> options = memberRepository.getGroupMembersExceptMe(memberId, groupId);
        return options;
    }

    public Question getCurrentQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) throw new CommonException(ErrorCode.QUESTION_NOT_FOUND);
        return questionRepository.getReferenceById(questionId);
    }

    public List<Question> getQuestionList() {
        List<Question> questions = questionRepository.getSelectedQuestion();
        if (questions.size() != 7) {
            throw new CommonException(ErrorCode.QUESTION_CNT_NOT_SEVEN);
        }
        return questions;
    }

    public Long getMemberCnt(Long groupId) {
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        return memberRepository.getMemberCnt(groupId);
    }
}
