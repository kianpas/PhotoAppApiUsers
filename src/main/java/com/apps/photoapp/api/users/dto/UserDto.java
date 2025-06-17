package com.apps.photoapp.api.users.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String password;

    private String email;

    private String userId;

    private String encPassword;
}
