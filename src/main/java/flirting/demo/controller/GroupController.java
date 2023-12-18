package flirting.demo.controller;

import flirting.demo.common.ApiStatus;
import flirting.demo.common.ResponseData;
import flirting.demo.common.StatusCode;
import flirting.demo.dto.GroupCreateRequest;
import flirting.demo.dto.GroupCreateResponse;
import flirting.demo.dto.GroupListResponse;
import flirting.demo.entity.Group;
import flirting.demo.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/group")
public class GroupController {
    private final GroupService groupService;

    @PostMapping(value = "/create", produces = "application/json")
    public ResponseEntity<Object> create(@RequestBody GroupCreateRequest groupCreateRequest){
        HttpHeaders httpHeaders = new HttpHeaders();
        try{
            Group group = groupService.create(groupCreateRequest);
            return new ResponseEntity<>(
                    new ResponseData(
                            new ApiStatus(StatusCode.OK, "그룹 생성 성공"),
                            new GroupCreateResponse(group.getId())
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

    @GetMapping(value = "/{memberId}", produces = "application/json")
    public ResponseEntity<Object> getGroups(@PathVariable Long memberId){
        HttpHeaders httpHeaders = new HttpHeaders();
        try{
            List<Group> groups = groupService.getGroups(memberId);
            return  new ResponseEntity<>(
                    new ResponseData(
                            new ApiStatus(StatusCode.OK, "그룹 조회 성공"),
                            new GroupListResponse(groups)
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
