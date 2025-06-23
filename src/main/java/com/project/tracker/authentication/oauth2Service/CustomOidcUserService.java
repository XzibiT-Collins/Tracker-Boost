package com.project.tracker.authentication.oauth2Service;

import com.project.tracker.models.Users;
import com.project.tracker.models.authmodels.AuthLog;
import com.project.tracker.models.authmodels.Role;
import com.project.tracker.repositories.RoleRepository;
import com.project.tracker.repositories.UsersRepository;
import com.project.tracker.sortingEnums.UserRolesEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final Logger logger = Logger.getLogger(CustomOidcUserService.class.getName());
    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;

    public CustomOidcUserService(UsersRepository usersRepository,
                                 RoleRepository roleRepository) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();

        if(!usersRepository.existsByEmail(email)){
            Role role = roleRepository.findByName(UserRolesEnum.ROLE_CONTRACTOR.toString());

            //create new user if user does not exist
            logger.info("Creating new user: " + email);

            Users user = Users.builder()
                    .email(email)
                    .name(name)
                    .password("N/A")
                    .role(role)
                    .build();

            user = usersRepository.save(user);
            logger.info("User created: " + user.getEmail());
        }
        Set<GrantedAuthority> authorities = new HashSet<>(oidcUser.getAuthorities());
        authorities.add(new SimpleGrantedAuthority(UserRolesEnum.ROLE_CONTRACTOR.toString()));

        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
