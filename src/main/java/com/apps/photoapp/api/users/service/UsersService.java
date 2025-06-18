package com.apps.photoapp.api.users.service;

import com.apps.photoapp.api.users.data.UserEntity;
import com.apps.photoapp.api.users.data.UsersRepository;
import com.apps.photoapp.api.users.dto.UserDto;
import com.apps.photoapp.api.users.utils.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {

    private final UsersRepository usersRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserMapper userMapper;

    /**
     * 유저 생성
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

        return userMapper.convertEntityToUserDto(userEntity);
    }

    public UserDto getUserDetailByEmail(String username) {
        UserEntity userEntity = usersRepository.findByEmail(username);
        if (userEntity == null) { throw new UsernameNotFoundException(username); }

        return userMapper.convertEntityToUserDto(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);

        if (userEntity == null) { throw new UsernameNotFoundException(username); }

        return new User(userEntity.getEmail(), userEntity.getEncPassword(),
                true, true, true, true,new ArrayList<>());
    }
}
