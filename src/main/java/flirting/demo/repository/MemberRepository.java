package flirting.demo.repository;

import flirting.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m join m.groups g where g.id = :groupId and m.id <> :memberId")
    public List<Member> getGroupMembersExceptMe(@Param("memberId") Long memberId,
                                                @Param("groupId") Long groupId);

    @Query("SELECT m FROM Member m JOIN m.groups g WHERE g.id IN " +
            "(SELECT g.id FROM Group g JOIN g.members m WHERE m.id = :memberId) " +
            "AND m.id <> :memberId")
    public List<Member> getAllMembersExceptMe(@Param("memberId") Long memberId);
}
