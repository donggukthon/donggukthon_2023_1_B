package flirting.demo.service;

import flirting.demo.dto.request.VoteGuessRequest;
import flirting.demo.dto.request.VoteRequest;
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

    public VoteResult getVoteResult(Long memberId, Long groupId, Long questionId) {
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

    public Long getTotalVoteCnt(Long groupId, Long questionId) {
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        if (!questionRepository.existsById(questionId)) throw new CommonException(ErrorCode.QUESTION_NOT_FOUND);

        return voteRepository.getTotalVoteCnt(groupId, questionId);
    }

    public Question getCurrentQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) throw new CommonException(ErrorCode.QUESTION_NOT_FOUND);

        return questionRepository.getReferenceById(questionId);
    }

    public List<Member> getOptionList(Long memberId, Long groupId) {
        if (!memberRepository.existsById(memberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);

        List<Member> options = memberRepository.getAllMembersExceptMe(memberId, groupId);
        if (options.stream().filter(op -> op.getId() == memberId).findAny().isPresent()) {
            throw new CommonException(ErrorCode.MYSELF_IN_OPTION);
        }
        return options;
    }

    public Integer getSnowFlakes(Long memberId) {
        if (!memberRepository.existsById(memberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);

        Member member = memberRepository.getReferenceById(memberId);
        return member.getSnowflake();
    }

    public boolean getIsCorrect(VoteGuessRequest voteGuessRequest) {
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

    public Member updateSnowFlakes(Long memberId) {
        if (!memberRepository.existsById(memberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);

        Member member = memberRepository.getReferenceById(memberId);
        if (member.getSnowflake() < 10) {
            throw new CommonException(ErrorCode.SNOWFLAKES_NOT_ENOUGH);
        }

        member.updateSnowflake(-10);
        Member _member = memberRepository.save(member);
        return _member;
    }

    public Member getMemberById(Long memberId) {
        if (!memberRepository.existsById(memberId)) throw new CommonException(ErrorCode.MEMBER_NOT_FOUND);
        return memberRepository.getReferenceById(memberId);
    }

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

    public Long getMemberCnt(Long groupId) {
        if (!groupRepository.existsById(groupId)) throw new CommonException(ErrorCode.GROUP_NOT_FOUND);
        return memberRepository.getMemberCnt(groupId);
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
