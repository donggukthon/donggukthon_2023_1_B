package flirting.demo.controller;

import flirting.demo.dto.common.ApiStatus;
import flirting.demo.dto.common.ResponseData;
import flirting.demo.dto.common.ResponseDto;
import flirting.demo.dto.common.StatusCode;
import flirting.demo.dto.request.InvitationAcceptRequest;
import flirting.demo.dto.response.InvitationAcceptResponse;
import flirting.demo.dto.response.InvitationResponse;
import flirting.demo.entity.Invitation;
import flirting.demo.service.InvitationService;
import flirting.demo.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        Invitation invitation = invitationService.createInvitation(inviterId, groupId);
        Long memberCnt = invitationService.getMemberCnt(groupId);
        InvitationResponse invitationResponse = InvitationResponse.builder()
                .invitation(invitation)
                .memberCnt(memberCnt)
                .build();

        return ResponseDto.ok(invitationResponse);
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseDto<?> accept(@RequestBody InvitationAcceptRequest invitationAcceptRequest) {
        InvitationAcceptResponse invitation = invitationService.acceptInvitation(invitationAcceptRequest);
        return ResponseDto.ok(invitation);

    }
}
