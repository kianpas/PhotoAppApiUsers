package com.apps.photoapp.api.users.ui.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserResponseModel {

    private String name;

    private String password;

    private String email;
}
