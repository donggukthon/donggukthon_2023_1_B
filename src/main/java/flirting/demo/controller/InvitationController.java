package flirting.demo.controller;

import flirting.demo.dto.common.ApiStatus;
import flirting.demo.dto.common.ResponseData;
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
    private final OAuthService oAuthService;

    @GetMapping(value = "/{inviterId}/{groupId}", produces = "application/json")
    public ResponseEntity<Object> getInvitationList(@PathVariable("inviterId") Long inviterId, @PathVariable("groupId") Long groupId) {
        // Todo: member id, group id로 초대장 조회

        HttpHeaders httpheaders = new HttpHeaders();
            Invitation invitation = invitationService.createInvitation(inviterId, groupId);
            Long memberCnt = invitationService.getMemberCnt(groupId);
            InvitationResponse invitationResponse = InvitationResponse.builder()
                    .invitation(invitation)
                    .memberCnt(memberCnt)
                    .build();

            return new ResponseEntity<>(
                    new ResponseData(
                            new ApiStatus(StatusCode.OK, "초대장 조회 성공"),
                            invitationResponse
                    ),
                    httpheaders, HttpStatus.OK
            );

    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<Object> accept(@RequestBody InvitationAcceptRequest invitationAcceptRequest){
        HttpHeaders httpHeaders = new HttpHeaders();
            InvitationAcceptResponse invitation = invitationService.acceptInvitation(invitationAcceptRequest);
            return new ResponseEntity<>(
                    new ResponseData(
                            new ApiStatus(StatusCode.OK, "초대 수락 성공"),
                            invitation
                    ),
                    httpHeaders, HttpStatus.OK
            );

    }
}
