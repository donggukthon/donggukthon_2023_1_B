package flirting.demo.repository;

import flirting.demo.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    @Query("select i from Invitation i where i.receiver.id = :receiverId")
    public List<Invitation> getInvitationsByReceiverId(@Param("receiverId") Long receiverId);
}
