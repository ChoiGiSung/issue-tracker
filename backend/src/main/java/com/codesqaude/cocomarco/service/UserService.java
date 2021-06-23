package com.codesqaude.cocomarco.service;

import com.codesqaude.cocomarco.common.config.JasyptConfig;
import com.codesqaude.cocomarco.common.exception.auth.DuplicateLocalUserId;
import com.codesqaude.cocomarco.common.exception.auth.NoPermissionUserException;
import com.codesqaude.cocomarco.common.exception.notfound.NotFoundUserException;
import com.codesqaude.cocomarco.domain.oauth.GitOAuth;
import com.codesqaude.cocomarco.domain.oauth.GitUserInfo;
import com.codesqaude.cocomarco.domain.oauth.dto.AccessToken;
import com.codesqaude.cocomarco.domain.oauth.dto.JwtResponse;
import com.codesqaude.cocomarco.domain.user.User;
import com.codesqaude.cocomarco.domain.user.UserRepository;
import com.codesqaude.cocomarco.domain.user.dto.UserLoginRequest;
import com.codesqaude.cocomarco.domain.user.dto.UserResponse;
import com.codesqaude.cocomarco.domain.user.dto.UserWrapper;
import com.codesqaude.cocomarco.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final GitOAuth oauthWeb;
    private final GitOAuth oauthIos;
    private final JasyptConfig jasyptConfig;


    public UserService(UserRepository userRepository, JasyptConfig jasyptConfig) {
        this.userRepository = userRepository;
        this.jasyptConfig = jasyptConfig;
        oauthIos = new GitOAuth(GitOAuthType.IOS);
        oauthWeb = new GitOAuth(GitOAuthType.WEB);
    }

    public User findById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    }

    @Transactional
    public JwtResponse loginIOS(String code) {
        return getJwtResponse(code, oauthIos);
    }

    @Transactional
    public JwtResponse loginWeb(String code) {
        return getJwtResponse(code, oauthWeb);
    }

    private JwtResponse getJwtResponse(String code, GitOAuth gitOAuth) {
        AccessToken accessToken = gitOAuth.accessToken(code);
        GitUserInfo userInfo = gitOAuth.userInfo(accessToken);
        return new JwtResponse(JwtUtils.create(insertUser(userInfo)));
    }

    // todo 리팩토링
    public UUID insertUser(GitUserInfo userInfo) {
        Optional<User> DBUser = userRepository.findByGithubId(userInfo.getId());

        if (DBUser.isPresent()) {
            User user = DBUser.get();
            user.update(userInfo.toEntity());
            return user.getId();
        }

        User user = userInfo.toEntity();
        return userRepository.save(user).getId();
    }

    @Transactional
    public void localJoin(UserLoginRequest userLoginRequest, int authCode) {
        Optional<User> DBUser = userRepository.findByLocalId(userLoginRequest.getLocalId());
        if (DBUser.isPresent()) {
            throw new DuplicateLocalUserId();
        }
        User user = User.localUser(userLoginRequest.getLocalId(), jasyptConfig.encrypt(userLoginRequest.getLocalPassword()), authCode);
        userRepository.save(user);
    }

    public JwtResponse localLogin(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByLocalIdAndAuthStatusTrue(userLoginRequest.getLocalId()).orElseThrow(NotFoundUserException::new);
        if (jasyptConfig.decrypt(user.getLocalPassword()).equals(userLoginRequest.getLocalPassword())) {
            return new JwtResponse(JwtUtils.create(user.getId()));
        }
        throw new NoPermissionUserException();
    }

    public UserWrapper findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<UserResponse> userResponses = users.stream().map(UserResponse::of).collect(Collectors.toList());
        return new UserWrapper(userResponses);
    }

    @Transactional
    public void activateLocalUser(String id, int code) {
        User user = userRepository.findByLocalId(id).orElseThrow(NotFoundUserException::new);
        if (user.sameAuthCode(code)) {
            user.changeStatus();
            return;
        }
        throw new NoPermissionUserException();
    }
}
