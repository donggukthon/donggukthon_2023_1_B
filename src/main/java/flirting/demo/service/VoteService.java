package flirting.demo.service;

import flirting.demo.dto.request.VoteGuessRequest;
import flirting.demo.dto.request.VoteRequest;
import flirting.demo.dto.response.VoteGuessDataResponse;
import flirting.demo.dto.response.VoteGuessResponse;
import flirting.demo.dto.response.VoteResultResponse;
import flirting.demo.entity.Group;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.entity.Vote;
import flirting.demo.exception.CommonException;
import flirting.demo.exception.ErrorCode;
import flirting.demo.repository.GroupRepository;
import flirting.demo.repository.MemberRepository;
import flirting.demo.repository.QuestionRepository;
import flirting.demo.repository.VoteRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    public Vote createVote(VoteRequest voteRequest) {
        Long questionId = voteRequest.getQuestionId();
        if (!questionRepository.existsById(questionId)) throw new CommonException(ErrorCode.QUESTION_NOT_FOUND);
        Question question = questionRepository.getReferenceById(questionId);

        Long voterId = voteRequest.getVoterId();
        if (!memberRepository.existsById(voterId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        Member voter = memberRepository.getReferenceById(voterId);

        Long selectedMemberId = voteRequest.getSelectedMemberId();
        if (!memberRepository.existsById(selectedMemberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        Member selectedMember = memberRepository.getReferenceById(selectedMemberId);

        Long groupId = voteRequest.getGroupId();
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        Group group = groupRepository.getReferenceById(groupId);

        Vote vote = voteRequest.toEntity(question, voter, selectedMember, group);
        Vote _vote = voteRepository.save(vote);

        return _vote;
    }

    public VoteResultResponse getTotalResult(Long memberId, Long groupId, Long questionId){
        VoteService.VoteResult voteResult = getVoteResult(memberId, groupId, questionId);
        Question currentQuestion = getCurrentQuestion(questionId);
        Integer snowflakes = getSnowFlakes(memberId);
        Long totalVoteCnt = getTotalVoteCnt(groupId, questionId);

        VoteResultResponse voteResultResponse = VoteResultResponse.builder()
                .snowflakes(snowflakes)
                .totalVoteCnt(totalVoteCnt)
                .question(currentQuestion)
                .voteResult(voteResult)
                .build();

        return voteResultResponse;
    }

    public VoteGuessDataResponse getGuessData(Long memberId, Long groupId, Long questionId){
        Question question = getCurrentQuestion(questionId);
        List<Member> options = getOptionList(memberId, groupId);
        Integer snowflakes = getSnowFlakes(memberId);
        Long memberCnt = getMemberCnt(groupId);

        VoteGuessDataResponse voteGuessDataResponse = VoteGuessDataResponse.builder()
                .snowflakes(snowflakes)
                .question(question)
                .memberCnt(memberCnt)
                .members(options)
                .build();

        return voteGuessDataResponse;
    }

    public VoteGuessResponse getGuessResult(VoteGuessRequest voteGuessRequest) {
        Long memberId = voteGuessRequest.getMemberId();
        Long selecteMemberId = voteGuessRequest.getSelectedMemberId();

        boolean isCorrect = getIsCorrect(voteGuessRequest);
        Member _member = updateSnowFlakes(memberId);

        if (!memberRepository.existsById(selecteMemberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        Member selectedMember = memberRepository.getReferenceById(selecteMemberId);

        VoteGuessResponse voteGuessResponse = VoteGuessResponse.builder()
                .member(_member)
                .selectedMember(selectedMember)
                .isCorrect(isCorrect)
                .snowflakes(_member.getSnowflake())
                .build();

        return voteGuessResponse;
    }

    // 이하 메소드 서비스 내부 호출
    private VoteResult getVoteResult(Long memberId, Long groupId, Long questionId) {
        if (!memberRepository.existsById(memberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        if (!questionRepository.existsById(questionId)) throw new CommonException(ErrorCode.QUESTION_NOT_FOUND);

        List<Member> voters = voteRepository.getMostVoted(groupId, questionId);
        Member mostVoted = voters.stream().findFirst().orElseThrow(() -> new CommonException(ErrorCode.VOTE_NOT_FOUND));

        Long mostVotedCnt = voteRepository.getMostVotedCnt(mostVoted.getId(), groupId, questionId);

        Long myVoteCnt = voteRepository.getMyVotes(memberId, groupId, questionId);
        Member currentMember = memberRepository.getReferenceById(memberId);

        return VoteResult.builder()
                .mostVoted(mostVoted.getUsername())
                .mostVotedCnt(Integer.parseInt(Long.toString(mostVotedCnt)))
                .memberName(currentMember.getUsername())
                .myVoteCnt(Integer.parseInt(Long.toString(myVoteCnt)))
                .build();

    }

    private Long getTotalVoteCnt(Long groupId, Long questionId) {
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        if (!questionRepository.existsById(questionId)) throw new CommonException(ErrorCode.QUESTION_NOT_FOUND);

        return voteRepository.getTotalVoteCnt(groupId, questionId);
    }

    private Question getCurrentQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) throw new CommonException(ErrorCode.QUESTION_NOT_FOUND);

        return questionRepository.getReferenceById(questionId);
    }

    private List<Member> getOptionList(Long memberId, Long groupId) {
        if (!memberRepository.existsById(memberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);

        List<Member> options = memberRepository.getAllMembersExceptMe(memberId, groupId);
        if (options.stream().filter(op -> op.getId() == memberId).findAny().isPresent()) {
            throw new CommonException(ErrorCode.MYSELF_IN_OPTION);
        }
        return options;
    }

    private Integer getSnowFlakes(Long memberId) {
        if (!memberRepository.existsById(memberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);

        Member member = memberRepository.getReferenceById(memberId);
        return member.getSnowflake();
    }

    private boolean getIsCorrect(VoteGuessRequest voteGuessRequest) {
        Long memberId = voteGuessRequest.getMemberId();
        Long selectedMemberId = voteGuessRequest.getSelectedMemberId();
        Long questionId = voteGuessRequest.getQuestionId();
        Long groupId = voteGuessRequest.getGroupId();

        if (!memberRepository.existsById(memberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);

        Member member = memberRepository.getReferenceById(memberId);

        if (member.getSnowflake() < 10) {
            throw new CommonException(ErrorCode.SNOWFLAKES_NOT_ENOUGH);
        }


        if (!memberRepository.existsById(selectedMemberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        if (!questionRepository.existsById(questionId)) throw new CommonException(ErrorCode.QUESTION_NOT_FOUND);

        Optional<Vote> vote = voteRepository.getVoteByGuessRequest(memberId, selectedMemberId, groupId, questionId);

        if (vote.isEmpty()) {
            return false;
        } else {
            member.updateSnowflake(+15);
            memberRepository.save(member);
            return true;
        }
    }

    private Member updateSnowFlakes(Long memberId) {
        if (!memberRepository.existsById(memberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);

        Member member = memberRepository.getReferenceById(memberId);
        if (member.getSnowflake() < 10) {
            throw new CommonException(ErrorCode.SNOWFLAKES_NOT_ENOUGH);
        }

        member.updateSnowflake(-10);
        Member _member = memberRepository.save(member);
        return _member;
    }

    private Long getMemberCnt(Long groupId) {
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        return memberRepository.getMemberCnt(groupId);
    }


    // Todo: repository에서 아래 클래스 형식 객체 반환하도록 수정
    @Getter
    public static class VoteRepoResult {
        String name;
        Integer cnt;

        @Builder
        public VoteRepoResult(String name, Long cnt) {
            this.name = name;
            this.cnt = Integer.parseInt(Long.toString(cnt));
        }
    }

    @Getter
    public static class VoteResult {
        String mostVoted;
        Integer mostVotedCnt;
        String memberName;
        Integer myVoteCnt;

        @Builder
        public VoteResult(String mostVoted, Integer mostVotedCnt, String memberName, Integer myVoteCnt) {
            this.mostVoted = mostVoted;
            this.mostVotedCnt = mostVotedCnt;
            this.memberName = memberName;
            this.myVoteCnt = myVoteCnt;
        }
    }
}
