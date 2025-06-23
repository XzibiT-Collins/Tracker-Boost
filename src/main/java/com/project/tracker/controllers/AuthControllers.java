package com.project.tracker.controllers;

import com.project.tracker.dto.requestDto.UserLoginRequestDto;
import com.project.tracker.dto.requestDto.UsersRequestDto;
import com.project.tracker.dto.responseDto.UserLoginResponseDto;
import com.project.tracker.dto.responseDto.UsersResponseDto;
import com.project.tracker.services.serviceInterfaces.UsersService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllers {
    UsersService usersService;

    public AuthControllers(UsersService usersService) {
        this.usersService = usersService;
    }

    @CacheEvict(value = {"developers","top5Developers"}, allEntries = true)
    @PostMapping("/register")
    public ResponseEntity<UsersResponseDto> register(@Valid @RequestBody UsersRequestDto user) {
        return ResponseEntity.ok(usersService.registerUser(user));
    }

    @PostMapping("/login")
    protected ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto user) {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.loginUser(user));
    }
}
