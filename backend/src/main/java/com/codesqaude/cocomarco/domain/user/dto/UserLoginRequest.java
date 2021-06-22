package com.codesqaude.cocomarco.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {

    private String localId;
    private String localPassword;

    @Override
    public String toString() {
        return "UserLoginRequest{" +
                "localId='" + localId + '\'' +
                ", localPassword='" + localPassword + '\'' +
                '}';
    }
}
