package flirting.demo.repository;

import flirting.demo.dto.response.MemberResponse;
import flirting.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m join m.groups g where g.id = :groupId and m.id <> :memberId")
    public List<Member> getGroupMembersExceptMe(@Param("memberId") Long memberId,
                                                @Param("groupId") Long groupId);

    @Query("SELECT m FROM Member m JOIN m.groups g WHERE g.id IN " +
            "(SELECT g.id FROM Group g JOIN g.members m WHERE m.id = :memberId) " +
            "AND m.id <> :memberId")
    public List<Member> getAllMembersExceptMe(@Param("memberId") Long memberId);

    @Query("SELECT m FROM Member m WHERE (:oauthId IS NULL OR m.oauthId = :oauthId)")
    Optional<Member> findByOauthId(String oauthId);
    @Query("SELECT m FROM Member m JOIN m.groups g WHERE g.id = :groupId and m.id <> :memberId")
    public List<Member> getAllMembersExceptMe(@Param("memberId") Long memberId,
                                              @Param("groupId") Long groupId);

    @Query("select count(m) from Member m join m.groups g where g.id = :groupId")
    public Long getMemberCnt(@Param("groupId") Long groupId);
}
