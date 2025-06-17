package com.apps.photoapp.api.users.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestModel {

    @NotNull(message = "비밀번호 필수")
    @Size(min = 8, max = 16, message = "8글자 이상")
    private String password;

    @NotNull(message = "이메일 필수")
    @Email
    private String email;
}
