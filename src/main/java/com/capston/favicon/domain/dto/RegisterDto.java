package com.capston.favicon.domain.dto;

import lombok.Data;

@Data
public class RegisterDto {

    private String username;
    private String password;
    private String passwordCheck; //회원가입 시 두번 패스워드 체크
}
