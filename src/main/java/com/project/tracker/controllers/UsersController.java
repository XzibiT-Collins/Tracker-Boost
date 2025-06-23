package com.project.tracker.controllers;

import com.project.tracker.dto.requestDto.UsersRequestDto;
import com.project.tracker.dto.responseDto.UsersResponseDto;
import com.project.tracker.services.serviceInterfaces.UsersService;
import com.project.tracker.sortingEnums.DeveloperSorting;
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
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        usersService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Users Deleted Successfully");
    }

    @PutMapping("/{id}")
    @CacheEvict(value = {"developers","top5Developers"}, allEntries = true)
    public ResponseEntity<UsersResponseDto> updateUser(@PathVariable int id, @Valid @RequestBody UsersRequestDto request ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usersService.updateUser(id,request));
    }

    @GetMapping("/{id}")
    @Cacheable(value = "developers", key = "#id")
    public ResponseEntity<UsersResponseDto> getUser(@PathVariable int id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usersService.getUserById(id));
    }


    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','MANAGER','CONTRACTOR')")
    @GetMapping("/me")
    protected ResponseEntity<UsersResponseDto> getCurrentLoggedInUser(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).body(usersService.getCurrentLoggedInUser(authentication));
    }



    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @Cacheable(value = "developers", key = "T(java.util.Objects).hash(#pageNumber, #sortBy)")
    public ResponseEntity<Page<UsersResponseDto>> getAllUsers(
            @RequestParam(required = false, defaultValue = "SORT_BY_ID") DeveloperSorting sortBy,
            @RequestParam(required = false, defaultValue = "0") int pageNumber
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usersService.getAllUsers(pageNumber,sortBy.getField()));
    }

    @Cacheable(value = "top5Developers")
    @GetMapping("/top5Developers")
    public ResponseEntity<Page<UsersResponseDto>> getTopDevelopers(){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usersService.getTopDevelopers());
    }
}
