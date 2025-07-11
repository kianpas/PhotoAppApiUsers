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
        return "Working on port " + environment.getProperty("local.server.port") + ", with token = "
                + environment.getProperty("token.secret");
    }



    @PostMapping
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails){

        log.debug("detail = {}", userDetails);
//        System.out.println("Client IP: " + userDetails);
        log.debug("Client IP = {}", userDetails);
        //모델 -> DTO
        UserDto userDto = userMapper.convertToUserDto(userDetails);
        System.out.println("userDto: " + userDto);
        //등록
        UserDto createdUser = usersService.createUser(userDto);
        System.out.println("createdUser: " + createdUser);
        CreateUserResponseModel responseModel = userMapper.convertToUserResModel(createdUser);

        log.info("responseModel: " + responseModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
    }

    @GetMapping("/users/test")
    public ResponseEntity<String> test(HttpServletRequest request) {
        System.out.println("Client IP: " + request.getRemoteAddr());
        return ResponseEntity.ok("OK");
    }
}
