package com.codesqaude.cocomarco.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserLoginRequest {

    @NotNull
    @NotEmpty
    private String localId;

    @NotNull
    @NotEmpty
    private String localPassword;

    @Email
    @NotNull
    @NotEmpty
    private String eMail;

    @Override
    public String toString() {
        return "UserLoginRequest{" +
                "localId='" + localId + '\'' +
                ", localPassword='" + localPassword + '\'' +
                ", eMail='" + eMail + '\'' +
                '}';
    }
}
