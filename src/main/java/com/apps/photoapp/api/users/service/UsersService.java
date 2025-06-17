package com.apps.photoapp.api.users.service;

import com.apps.photoapp.api.users.data.UserEntity;
import com.apps.photoapp.api.users.data.UsersRepository;
import com.apps.photoapp.api.users.dto.UserDto;
import com.apps.photoapp.api.users.utils.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserMapper userMapper;

    /**
     * 유저 생성
     * @param userDetails
     * @return
     */
    @Transactional
    public UserDto createUser(UserDto userDetails) {
        //1. 비밀번호 암호화
        userDetails.setEncPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        //2. DTO에 UUID 추가 (DTO는 불변 객체이므로 새로운 인스턴스로 생성)
        UserDto processedUserDto = userDetails.toBuilder()
                .userId(UUID.randomUUID().toString())
                .build();


        //3. DTO -> 엔티티 전환
        UserEntity userEntity = userMapper.convertToUserEntity(processedUserDto);



        //4. 등록
        usersRepository.save(userEntity);

        //5. 리턴객체 생성
        UserDto responDto = userMapper.convertEntityToUserDto(userEntity);

        return responDto;
    }

}
