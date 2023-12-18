package flirting.demo.repository;

import flirting.demo.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("select g from Group g join g.members m where m.id = :memberId")
    public List<Group> getGroupsByMemberId(@Param("memberId") Long memberId);
}
