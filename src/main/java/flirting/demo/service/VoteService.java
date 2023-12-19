package flirting.demo.service;

import flirting.demo.common.CustomException;
import flirting.demo.common.StatusCode;
import flirting.demo.dto.VoteRequest;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.entity.Vote;
import flirting.demo.repository.MemberRepository;
import flirting.demo.repository.QuestionRepository;
import flirting.demo.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.internal.log.SubSystemLogging;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    public Vote createVote(VoteRequest voteRequest){
        Question question = questionRepository.getReferenceById(voteRequest.getQuestionId());
        Member voter = memberRepository.getReferenceById(voteRequest.getVoterId());
        Member selectedMember = memberRepository.getReferenceById(voteRequest.getSelectedMemberId());

        Vote vote = voteRequest.toEntity(question, voter, selectedMember);

        Vote _vote = voteRepository.save(vote);

        return _vote;
    }

    public VoteResult getVoteResult(Long memberId, Long questionId) {
        List<Member> voters = voteRepository.getMostVoted(questionId);
        Member mostVoted = voters.stream().findFirst().orElseThrow(() -> new CustomException(StatusCode.NO_SELECTED_VOTE));

        Long mostVotedCnt = voteRepository.getMostVotedCnt(mostVoted.getId(), questionId);

        Long myVoteCnt = voteRepository.getMyVotes(memberId, questionId);
        // VoteRepoResult myVoteResult = myVoteResults.stream().findFirst().orElseThrow(() -> new CustomException(StatusCode.NO_SELECTED_VOTE));
        Member currentMember = memberRepository.getReferenceById(memberId);

        return VoteResult.builder()
                .mostVoted(mostVoted.getUsername())
                .mostVotedCnt(Integer.parseInt(Long.toString(mostVotedCnt)))
                .memberName(currentMember.getUsername())
                .myVoteCnt(Integer.parseInt(Long.toString(myVoteCnt)))
                .build();

    }

    public Question getCurrentQuestion(Long questionId) {
        return questionRepository.getReferenceById(questionId);
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
