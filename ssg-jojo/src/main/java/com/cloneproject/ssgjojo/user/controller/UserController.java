package com.cloneproject.ssgjojo.user.controller;

import com.cloneproject.ssgjojo.user.domain.User;
import com.cloneproject.ssgjojo.user.dto.*;
import com.cloneproject.ssgjojo.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final IUserService iUserService;

    @PostMapping("/user/signup") // 회원가입
    public User addUser(@RequestBody UserSignupDto userSignupDto) {

        return iUserService.addUser(userSignupDto);
    }

    @GetMapping("/user/get/{id}") // 마이페이지
    public UserGetIdDto getUser(@PathVariable Long id) {
        return iUserService.getUserById(id);
    }

    @GetMapping("/user/getAll") // 회원 조회
    public List<UserGetAllDto> getAll() {
        return iUserService.getAll();
    }

    @PutMapping("/user/edit") // 회원 정보 수정
    public User editUser(@RequestBody UserEditDto userEditDto) {
        return iUserService.editUser(userEditDto);
    }

    @PutMapping("/user/delete") // 회원 탈퇴
    public void deleteUser(@PathVariable UserDeleteDto userDeleteDto) {
        iUserService.deleteUser(userDeleteDto);
    }

}