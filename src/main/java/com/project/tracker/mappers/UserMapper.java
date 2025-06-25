package com.project.tracker.mappers;

import com.project.tracker.dto.requestDto.UsersRequestDto;
import com.project.tracker.dto.responseDto.UsersResponseDto;
import com.project.tracker.models.Users;
import com.project.tracker.models.authmodels.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static Users mapToUser(UsersRequestDto requestDto){
        return Users.builder()
                .name(requestDto.name())
                .email(requestDto.email())
                .password(requestDto.password())
                .skills(requestDto.skills())
                .role(Role.builder().name(requestDto.role().toString()).build())
                .build();
    }

    public static UsersResponseDto mapToUserResponseDto(Users user){
        return UsersResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .skills(user.getSkills())
                .tasks(user.getTasks())
                .role(user.getRole().getName())
                .build();
    }
}
