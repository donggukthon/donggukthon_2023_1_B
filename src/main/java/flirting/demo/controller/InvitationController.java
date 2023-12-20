package flirting.demo.controller;

import flirting.demo.common.ApiStatus;
import flirting.demo.common.ResponseData;
import flirting.demo.common.StatusCode;
import flirting.demo.dto.response.InvitationListResponse;
import flirting.demo.dto.request.InvitationAcceptRequest;
import flirting.demo.dto.response.InvitationAcceptResponse;
import flirting.demo.dto.request.InvitationShareRequest;
import flirting.demo.entity.Invitation;
import flirting.demo.entity.Member;
import flirting.demo.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invitation")
public class InvitationController {
    private final InvitationService invitationService;

    @GetMapping(value = "/invitation/{memberId}/{groupId}", produces = "application/json")
    public ResponseEntity<Object> getInvitationList(@PathVariable("memberId") Long memberId, @PathVariable("groupId") Long groupId) {
        // Todo: member id, group id로 초대장 조회
        HttpHeaders httpheaders = new HttpHeaders();
        try {
            List<Invitation> invitations = invitationService.getInvitationList(memberId);
            InvitationListResponse homeResponse = new InvitationListResponse(invitations);
            return new ResponseEntity<>(new ResponseData(
                    new ApiStatus(StatusCode.OK, "초대장 조회 성공"),
                    homeResponse
            ), httpheaders, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage()),
                    httpheaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "", produces = "application/json")
    public ResponseEntity<ApiStatus> share(@RequestBody InvitationShareRequest invitationShareRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            Member member = invitationService.shareInvitation(invitationShareRequest);

            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.OK, "눈송이 증정 성공"),
                    httpHeaders, HttpStatus.OK
            );
        }catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage()),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<Object> accept(@RequestBody InvitationAcceptRequest invitationAcceptRequest){
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            Invitation invitation = invitationService.acceptInvitation(invitationAcceptRequest);
            return new ResponseEntity<>(
                    new ResponseData(
                            new ApiStatus(StatusCode.OK, "초대 수락 성공"),
                            InvitationAcceptResponse.builder()
                                    .memberName(invitation.getReceiver().getUsername())
                                    .groupName(invitation.getGroup().getName())
                                    .build()
                    ),
                    httpHeaders, HttpStatus.OK
            );
        }catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage()),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
