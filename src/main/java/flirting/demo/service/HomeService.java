package flirting.demo.service;

import flirting.demo.entity.Invitation;
import flirting.demo.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class HomeService {
    private final InvitationRepository invitationRepository;

    public List<Invitation> getHome(Long receiverId) {
        try {
            List<Invitation> invitations = invitationRepository.getInvitationsByReceiverId(receiverId);
            return invitations;
        }
        catch (RuntimeException e) {
            throw new RuntimeException("사용자의 초대장을 조회할 수 없습니다.");
        }
    }
}
