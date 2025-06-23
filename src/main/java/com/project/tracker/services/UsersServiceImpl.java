package com.project.tracker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.tracker.authentication.jwtService.JwtService;
import com.project.tracker.dto.requestDto.UserLoginRequestDto;
import com.project.tracker.dto.requestDto.UsersRequestDto;
import com.project.tracker.dto.responseDto.UserLoginResponseDto;
import com.project.tracker.dto.responseDto.UsersResponseDto;
import com.project.tracker.exceptions.customExceptions.InvalidLoginDetailsException;
import com.project.tracker.exceptions.customExceptions.UserRoleNotFoundException;
import com.project.tracker.exceptions.customExceptions.UserNotFoundException;
import com.project.tracker.exceptions.customExceptions.UserAlreadyExistException;
import com.project.tracker.models.AuditLog;
import com.project.tracker.models.Users;
import com.project.tracker.models.authmodels.Role;
import com.project.tracker.repositories.RoleRepository;
import com.project.tracker.repositories.UsersRepository;
import com.project.tracker.services.serviceInterfaces.AuditLogService;
import com.project.tracker.services.serviceInterfaces.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.logging.Logger;

@Service
public class UsersServiceImpl implements UsersService {
    private final Logger logger = Logger.getLogger(UsersServiceImpl.class.getName());
    private final ObjectMapper objectMapper;
    private final UsersRepository usersRepository;
    private final AuditLogService auditLogService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    public UsersServiceImpl(ObjectMapper objectMapper,
                            AuditLogService auditLogService,
                            PasswordEncoder passwordEncoder,
                            RoleRepository roleRepository,
                            UsersRepository usersRepository,
                            AuthenticationProvider authenticationProvider,
                            JwtService jwtService) {
        this.objectMapper = objectMapper;
        this.auditLogService = auditLogService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.usersRepository = usersRepository;
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
    }

    @Override
    public UsersResponseDto registerUser(UsersRequestDto requestDto) {
        if(requestDto == null){
            throw new UserNotFoundException("Users cannot be null");
        }
        if(usersRepository.existsByEmail(requestDto.email())){
            throw new UserAlreadyExistException("User with email: " + requestDto.email() + " already exists");
        }

        Role role = roleRepository.findByName(requestDto.role().toString());

        if(role == null){
            throw new UserRoleNotFoundException("Role with name: " + requestDto.role() + " not found");
        }

        Users users = Users.builder().
                name(requestDto.name()).
                email(requestDto.email()).
                password(passwordEncoder.encode(requestDto.password())).
                role(role).
                skills(requestDto.skills())
                .build();
        return objectMapper.convertValue(usersRepository.save(users), UsersResponseDto.class);
    }

    @Override
    public UserLoginResponseDto loginUser(UserLoginRequestDto request){
        Authentication authentication = authenticationProvider.
                authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        //check if the user is successfully authenticated
        if(authentication.isAuthenticated()){
            System.out.println(authentication.getPrincipal() + "User logged in");
            return new UserLoginResponseDto(
                    request.email(),
                    jwtService.generateToken(request.email()));
        }
        throw new InvalidLoginDetailsException("Invalid login credentials");
    }

    @Override
    public UsersResponseDto getCurrentLoggedInUser(Authentication authentication) {
        Users users = usersRepository.findByEmail(authentication.getName());
        logger.info("Current Logged in user: " + users.getName() + " " + users.getEmail());

        return objectMapper.convertValue(users, UsersResponseDto.class);
    }

    @Override
    public void deleteUser(int id) {
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Users with ID: " + id + " not found"));

        usersRepository.delete(users);
        logAudit("Delete Users", String.valueOf(users.getId()), users.getName(), users);
    }

    @Override
    public UsersResponseDto updateUser(int id, UsersRequestDto requestDto) {
        if (!usersRepository.existsById(id)) {
            throw new UserNotFoundException("Users with ID: " + id + " not found");
        }

        Users updatedUsers = Users.builder()
                .id(id)
                .name(requestDto.name())
                .email(requestDto.email())
                .skills(requestDto.skills())
                .build();

        Users savedUsers = usersRepository.save(updatedUsers);
        logAudit("Update Users", String.valueOf(savedUsers.getId()), savedUsers.getName(), savedUsers);

        return objectMapper.convertValue(savedUsers, UsersResponseDto.class);
    }

    @Override
    public UsersResponseDto getUserById(int id) {
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Users with ID: " + id + " not found"));

        logAudit("Get Users By ID", String.valueOf(users.getId()), users.getName(), users);
        return objectMapper.convertValue(users, UsersResponseDto.class);
    }

    @Override
    public Page<UsersResponseDto> getAllUsers(int pageNumber, String sortBy) {
        int paginateBy = 10;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNumber, paginateBy, sort);

        Page<Users> developers = usersRepository.findAll(pageable);

        logAudit("Get all Developers" + paginateBy, "PAGE_"+pageNumber, "None", "Users");
        return developers.map(developer -> objectMapper.convertValue(developer, UsersResponseDto.class));
    }

    @Override
    public Page<UsersResponseDto> getTopDevelopers() {
        int size = 5;
        Pageable pageable = PageRequest.of(0, size);
        Page<Users> top5Developers = usersRepository.findTop5ByOrderByTasksCountDesc(pageable);

        logAudit("Get Top 5 Developers", "PAGE_"+0, "None", "Users");
        return top5Developers
                .map(developer -> objectMapper.convertValue(developer, UsersResponseDto.class));
    }













    private void logAudit(String actionType, String entityId, String actorName, Object entity) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            payload = "Could not serialize payload: " + e.getMessage();
        }

        auditLogService.addAuditLog(AuditLog.builder()
                .actionType(actionType)
                .entityId(String.valueOf(entityId))
                .actorName(actorName)
                .payload(payload)
                .timestamp(Date.valueOf(LocalDate.now()))
                .build());
    }
}
