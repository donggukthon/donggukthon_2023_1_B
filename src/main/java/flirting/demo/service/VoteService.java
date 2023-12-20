package flirting.demo.service;

import flirting.demo.common.CustomException;
import flirting.demo.common.StatusCode;
import flirting.demo.dto.VoteGuessRequest;
import flirting.demo.dto.request.VoteRequest;
import flirting.demo.entity.Group;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.entity.Vote;
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

    public Vote createVote(VoteRequest voteRequest){
        Question question = questionRepository.getReferenceById(voteRequest.getQuestionId());
        Member voter = memberRepository.getReferenceById(voteRequest.getVoterId());
        Member selectedMember = memberRepository.getReferenceById(voteRequest.getSelectedMemberId());
        Group group = groupRepository.getReferenceById(voteRequest.getGroupId());

        Vote vote = voteRequest.toEntity(question, voter, selectedMember, group);

        Vote _vote = voteRepository.save(vote);

        return _vote;
    }

    public VoteResult getVoteResult(Long memberId, Long groupId, Long questionId) {
        List<Member> voters = voteRepository.getMostVoted(groupId, questionId);
        Member mostVoted = voters.stream().findFirst().get();

        Long mostVotedCnt = voteRepository.getMostVotedCnt(mostVoted.getId(), groupId, questionId);

        Long myVoteCnt = voteRepository.getMyVotes(memberId, groupId, questionId);
        // VoteRepoResult myVoteResult = myVoteResults.stream().findFirst().orElseThrow(() -> new CustomException(StatusCode.NO_SELECTED_VOTE));
        Member currentMember = memberRepository.getReferenceById(memberId);

        return VoteResult.builder()
                .mostVoted(mostVoted.getUsername())
                .mostVotedCnt(Integer.parseInt(Long.toString(mostVotedCnt)))
                .memberName(currentMember.getUsername())
                .myVoteCnt(Integer.parseInt(Long.toString(myVoteCnt)))
                .build();

    }

    public Long getTotalVoteCnt(Long groupId, Long questionId) {
        return voteRepository.getTotalVoteCnt(groupId, questionId);
    }

    public Question getCurrentQuestion(Long questionId) {
        return questionRepository.getReferenceById(questionId);
    }

    // Todo: 중복 제거
    public List<Member> getOptionList(Long memberId, Long groupId) {

        // 내가 속한 그룹에 있는 모든 사람들 조회
        List<Member> options = memberRepository.getAllMembersExceptMe(memberId, groupId);
        // 그룹에 멤버가 나 혼자 -> 제거하고 멤버 수 반환
//        if (options.size() == 0) {
//            throw new CustomException(StatusCode.NO_OTHER_MEMBERS_IN_GROUP);
//        }
        // 자기 자신 제외하고 주기
        if (options.stream().filter(op -> op.getId() == memberId).findAny().isPresent()) {
            throw new CustomException(StatusCode.MYSELF_IN_OPTIONS);
        }
        return options;
    }

    public Integer getSnowFlakes(Long memberId){
        Member member = memberRepository.getReferenceById(memberId);
        return member.getSnowflake();
    }

    public boolean getIsCorrect(VoteGuessRequest voteGuessRequest) {
        Long memberId = voteGuessRequest.getMemberId();
        Long selectedMemberId = voteGuessRequest.getSelectedMemberId();
        Long questionId = voteGuessRequest.getQuestionId();
        Long groupId = voteGuessRequest.getGroupId();

        Optional<Vote> vote = voteRepository.getVoteByGuessRequest(memberId, selectedMemberId, groupId, questionId);

        if (vote.isEmpty()) {
            return false;
        } else if (vote.isPresent()) {
            Member member = memberRepository.getReferenceById(memberId);
            member.updateSnowflake(+15);
            memberRepository.save(member);
            return true;
        }
        else {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Member updateSnowFlakes(Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        if (member.getSnowflake() < 10){
            throw new CustomException(StatusCode.SNOWFLAKE_NOT_ENOUGH);
        }
        member.updateSnowflake(-10);
        Member _member = memberRepository.save(member);
        return _member;
    }

    public Member getMemberById(Long memberId) {
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
        return memberRepository.getMemberCnt(groupId);
    }

    @Getter
    public static class VoteResult {
        String mostVoted;
        Integer mostVotedCnt;
        String memberName;
        Integer myVoteCnt;

        @Builder
        public VoteResult(String mostVoted,Integer mostVotedCnt, String memberName, Integer myVoteCnt) {
            this.mostVoted = mostVoted;
            this.mostVotedCnt = mostVotedCnt;
            this.memberName = memberName;
            this.myVoteCnt = myVoteCnt;
        }
    }
}
