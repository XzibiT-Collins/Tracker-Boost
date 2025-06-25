package com.project.tracker.controllers;

import com.project.tracker.dto.requestDto.UsersRequestDto;
import com.project.tracker.dto.responseDto.ApiResponseDto;
import com.project.tracker.dto.responseDto.TaskResponseDto;
import com.project.tracker.dto.responseDto.UsersResponseDto;
import com.project.tracker.services.serviceInterfaces.UsersService;
import com.project.tracker.sortingEnums.DeveloperSorting;
import com.project.tracker.sortingEnums.TaskSorting;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = {"developers","top5Developers"}, allEntries = true)
    public ResponseEntity<ApiResponseDto<String>> deleteUser(@PathVariable int id){
        usersService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success("user deleted successfully",HttpStatus.OK.value()));
    }

    @PutMapping("/{id}")
    @CacheEvict(value = {"developers","top5Developers"}, allEntries = true)
    public ResponseEntity<ApiResponseDto<UsersResponseDto>> updateUser(@PathVariable int id, @Valid @RequestBody UsersRequestDto request ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success(usersService.updateUser(id,request),HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Cacheable(value = "developers", key = "#id")
    public ResponseEntity<ApiResponseDto<UsersResponseDto>> getUser(@PathVariable int id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success(usersService.getUserById(id),HttpStatus.OK.value()));
    }


    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','MANAGER','CONTRACTOR')")
    @GetMapping("/me")
    protected ResponseEntity<ApiResponseDto<UsersResponseDto>> getCurrentLoggedInUser(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(usersService.getCurrentLoggedInUser(authentication),HttpStatus.OK.value()));
    }



    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @Cacheable(value = "developers", key = "T(java.util.Objects).hash(#pageNumber, #sortBy)")
    public ResponseEntity<ApiResponseDto<Page<UsersResponseDto>>> getAllUsers(
            @RequestParam(required = false, defaultValue = "SORT_BY_ID") DeveloperSorting sortBy,
            @RequestParam(required = false, defaultValue = "0") int pageNumber
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success(usersService.getAllUsers(pageNumber,sortBy.getField()),HttpStatus.OK.value()));
    }

    @Cacheable(value = "top5Developers")
    @GetMapping("/top5Developers")
    public ResponseEntity<ApiResponseDto<Page<UsersResponseDto>>> getTopDevelopers(){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success(usersService.getTopDevelopers(),HttpStatus.OK.value()));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER','ROLE_DEVELOPER')")
    @Cacheable(value = "userTasks")
    @GetMapping("/{userId}/tasks")
    public ResponseEntity<ApiResponseDto<Page<TaskResponseDto>>> getUserTasks(
            @PathVariable int userId,
            @RequestParam(required = false, defaultValue = "SORT_BY_TITLE") TaskSorting sortBy,
            @RequestParam(required = false, defaultValue = "0") int pageNumber
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success(usersService
                        .getAllUserTasks(userId,sortBy.getField(),pageNumber),HttpStatus.OK.value()));
    }
}
