package com.project.tracker.services.serviceInterfaces;

import com.project.tracker.dto.requestDto.UserLoginRequestDto;
import com.project.tracker.dto.requestDto.UsersRequestDto;
import com.project.tracker.dto.responseDto.UserLoginResponseDto;
import com.project.tracker.dto.responseDto.UsersResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface UsersService {
//    UsersResponseDto addDeveloper(UsersRequestDto requestDto);
    void deleteUser(int id);
    UsersResponseDto updateUser(int id, UsersRequestDto requestDto);
    UsersResponseDto getUserById(int id);
    Page<UsersResponseDto> getAllUsers(int pageNumber, String sortBy);
    Page<UsersResponseDto> getTopDevelopers();
    UsersResponseDto registerUser(UsersRequestDto requestDto);
    UserLoginResponseDto loginUser(UserLoginRequestDto requestDto);

    UsersResponseDto getCurrentLoggedInUser(Authentication authentication);
}
