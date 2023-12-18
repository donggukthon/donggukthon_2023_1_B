package flirting.demo.service;

import flirting.demo.dto.VoteRequest;
import flirting.demo.entity.Member;
import flirting.demo.entity.Question;
import flirting.demo.entity.Vote;
import flirting.demo.repository.MemberRepository;
import flirting.demo.repository.QuestionRepository;
import flirting.demo.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
