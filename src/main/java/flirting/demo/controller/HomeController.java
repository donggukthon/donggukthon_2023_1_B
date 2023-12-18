package flirting.demo.controller;

import flirting.demo.common.ApiStatus;
import flirting.demo.common.ResponseData;
import flirting.demo.common.StatusCode;
import flirting.demo.dto.HomeResponse;
import flirting.demo.entity.Invitation;
import flirting.demo.service.HomeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @GetMapping(value = "/home/{memberId}", produces = "application/json")
    public ResponseEntity<Object> getHome(@PathVariable("memberId") Long memberId) {
        HttpHeaders httpheaders = new HttpHeaders();
        try {
            List<Invitation> invitations = homeService.getHome(memberId);
            HomeResponse homeResponse = new HomeResponse(invitations);
            return new ResponseEntity<>(new ResponseData(
                    new ApiStatus(StatusCode.OK, "홈 조회 성공"),
                    homeResponse
            ), httpheaders, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ApiStatus(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage()),
                    httpheaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
