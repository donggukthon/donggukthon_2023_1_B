package flirting.demo.controller;

import flirting.demo.common.ApiStatus;
import flirting.demo.common.ResponseData;
import flirting.demo.common.StatusCode;
import flirting.demo.dto.InvitationAcceptRequest;
import flirting.demo.dto.InvitationAcceptResponse;
import flirting.demo.dto.InvitationShareRequest;
import flirting.demo.entity.Invitation;
import flirting.demo.entity.Member;
import flirting.demo.service.InvitationService;
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
