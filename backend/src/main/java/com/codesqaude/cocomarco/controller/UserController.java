package com.codesqaude.cocomarco.controller;

import com.codesqaude.cocomarco.domain.oauth.dto.JwtResponse;
import com.codesqaude.cocomarco.domain.user.dto.UserLoginRequest;
import com.codesqaude.cocomarco.domain.user.dto.UserWrapper;
import com.codesqaude.cocomarco.service.MailService;
import com.codesqaude.cocomarco.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    @PostMapping("/git/login/ios")
    public JwtResponse iOSLogin(@RequestParam(name = "code") String code) {
        return userService.loginIOS(code);
    }

    @PostMapping("/git/login/web")
    public JwtResponse webLogin(@RequestParam(name = "code") String code) {
        return userService.loginWeb(code);
    }

    @PostMapping("/users/login")
    public JwtResponse localLogin(UserLoginRequest userLoginRequest) {
        return userService.localLogin(userLoginRequest);
    }

    @PostMapping("/users/join")
    public void localJoin(@Valid UserLoginRequest userLoginRequest) {
        log.debug(userLoginRequest.toString());
        int code = mailService.getRandom();
        mailService.sendMail(userLoginRequest.getEMail(), code);
        userService.localJoin(userLoginRequest, code);
    }


    @PutMapping("/users/active")
    public void activateLocalUser(@RequestParam(name = "id") String id, @RequestParam(name = "code") int code) {
        userService.activateLocalUser(id, code);
    }

    @GetMapping("/users")
    public UserWrapper findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }
}
