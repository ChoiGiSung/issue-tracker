package com.codesqaude.cocomarco.controller;

import com.codesqaude.cocomarco.domain.oauth.dto.JwtResponse;
import com.codesqaude.cocomarco.domain.user.dto.UserLoginRequest;
import com.codesqaude.cocomarco.domain.user.dto.UserWrapper;
import com.codesqaude.cocomarco.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    //클라이언트가
    @PostMapping("/git/login/ios")
    public JwtResponse loginIos(@RequestParam(name = "code") String code) {
        return userService.loginIOS(code);
    }

    //클라이언트가
    @PostMapping("/git/login/web")
    public JwtResponse loginWeb(@RequestParam(name = "code") String code) {
        return userService.loginWeb(code);
    }

    @PostMapping("/join")
    public JwtResponse join(UserLoginRequest userLoginRequest) {
        log.debug(userLoginRequest.toString());
        return userService.localJoin(userLoginRequest);
    }

    @PostMapping("/login")
    public JwtResponse login(UserLoginRequest userLoginRequest) {
        return userService.localLogin(userLoginRequest);
    }

    @GetMapping("/users")
    public UserWrapper findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }
}
