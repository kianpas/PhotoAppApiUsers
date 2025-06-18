package com.apps.photoapp.api.users.utils;

import com.apps.photoapp.api.users.data.UserEntity;
import com.apps.photoapp.api.users.dto.UserDto;
import com.apps.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.apps.photoapp.api.users.ui.model.CreateUserResponseModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    //DTO -> 엔티티
    public UserEntity convertToUserEntity(UserDto userDto) {
        return UserEntity.builder()
                .name(userDto.getName())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .userId(userDto.getUserId())
                .encPassword(userDto.getEncPassword())
                .build();
    }

    //요청 모델 -> 엔티티
    public UserDto convertToUserDto(CreateUserRequestModel userDetails) {
        return UserDto.builder()
                .name(userDetails.getName())
                .password(userDetails.getPassword())
                .email(userDetails.getEmail())
                .build();
    }

    //DTO -> 응답 모델
    public CreateUserResponseModel convertToUserResModel(UserDto userDto) {
        return CreateUserResponseModel.builder()
                .name(userDto.getName())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .build();
    }

    //엔티티 -> DTO
    public UserDto convertEntityToUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .userId(userEntity.getUserId())
                .build();
    }

}
