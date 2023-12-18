package flirting.demo.controller;

import flirting.demo.common.ApiStatus;
import flirting.demo.common.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    @GetMapping("/health")
    private ResponseEntity<ApiStatus> healthCheck() {
        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(
                new ApiStatus(StatusCode.OK, "요청 확인"),
                httpHeaders, HttpStatus.OK);

    }

}
