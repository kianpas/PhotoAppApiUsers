package com.apps.photoapp.api.users.controller;


import com.apps.photoapp.api.users.dto.UserDto;
import com.apps.photoapp.api.users.service.UsersService;
import com.apps.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.apps.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.apps.photoapp.api.users.utils.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {

    //서비스
    private final UsersService usersService;

    //
    private final UserMapper userMapper;

    private final Environment environment;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port " + environment.getProperty("local.server.port");
    }


    @PostMapping(
            consumes = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails){

        log.debug("detail = {}", userDetails);
        System.out.println("Client IP: " + userDetails);
        //모델 -> DTO
        UserDto userDto = userMapper.convertToUserDto(userDetails);
        //등록
        UserDto createdUser = usersService.createUser(userDto);

        CreateUserResponseModel responseModel = userMapper.convertToUserResModel(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
    }

    @GetMapping("/users/test")
    public ResponseEntity<String> test(HttpServletRequest request) {
        System.out.println("Client IP: " + request.getRemoteAddr());
        return ResponseEntity.ok("OK");
    }
}
