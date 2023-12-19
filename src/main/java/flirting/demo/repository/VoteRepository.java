package flirting.demo.repository;

import flirting.demo.entity.Member;
import flirting.demo.entity.Vote;
import flirting.demo.service.VoteService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {


    // Todo: vote repo result 사용하는걸로 수정
    @Query("select count(v) " +
            "from Vote v where v.selectedMember.id = :memberId and v.question.id = :questionId group by v.selectedMember.id order by count(v) desc")
    public Long getMostVotedCnt(@Param("memberId") Long memberId, @Param("questionId") Long questionId);

    @Query("select v.selectedMember " +
            "from Vote v where v.question.id = :questionId group by v.selectedMember.id order by count(v) desc")
    public List<Member> getMostVoted(@Param("questionId") Long questionId);

    @Query("select count(v) from Vote v where v.selectedMember.id = :memberId and v.question.id = :questionId")
    public Long getMyVotes(@Param("memberId") Long memberId,
                                                 @Param("questionId") Long questionId);

}
