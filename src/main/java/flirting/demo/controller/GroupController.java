package flirting.demo.controller;

import flirting.demo.dto.common.ResponseDto;
import flirting.demo.dto.request.GroupCreateRequest;
import flirting.demo.dto.response.GroupCreateResponse;
import flirting.demo.dto.response.GroupListResponse;
import flirting.demo.entity.Group;
import flirting.demo.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/group")
public class GroupController {
    private final GroupService groupService;

    @PostMapping(value = "/create", produces = "application/json")
    public ResponseDto<?> create(@RequestBody GroupCreateRequest groupCreateRequest){
        // try-catch는 필요하지 않음: global exception handler에서 잡아냄
        // Todo: memberId=0인 경우 예외 처리는 잘 되지만 http status는 200으로 오는거 수정
        return ResponseDto.created(groupService.create(groupCreateRequest));
    }

    @GetMapping(value = "/{memberId}", produces = "application/json")
    public ResponseDto<?> getGroups(@PathVariable Long memberId){
            return ResponseDto.ok(groupService.getGroups(memberId));
    }

}
