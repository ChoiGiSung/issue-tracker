package com.codesqaude.cocomarco.common.exception.auth;

import com.codesqaude.cocomarco.common.exception.ErrorCode;

public class DuplicateLocalUserId extends AuthException {

    public DuplicateLocalUserId() {
        super(ErrorCode.DUPLICATE_LOCAL_USER_ID);
    }
}
