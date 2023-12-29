package flirting.demo.controller;

import flirting.demo.dto.common.ResponseDto;
import flirting.demo.dto.request.InvitationAcceptRequest;
import flirting.demo.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invitation")
public class InvitationController {
    private final InvitationService invitationService;

    /**
     * member id, group id로 초대장 조회하는 메서드
     *
     * @param inviterId
     * @param groupId
     * @return
     */
    @GetMapping(value = "/{inviterId}/{groupId}", produces = "application/json")
    public ResponseDto<?> getInvitationList(@PathVariable("inviterId") Long inviterId, @PathVariable("groupId") Long groupId) {
        return ResponseDto.ok(invitationService.getInvitationList(inviterId, groupId));
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseDto<?> accept(@RequestBody InvitationAcceptRequest invitationAcceptRequest) {
        return ResponseDto.ok(invitationService.acceptInvitation(invitationAcceptRequest));

    }
}
