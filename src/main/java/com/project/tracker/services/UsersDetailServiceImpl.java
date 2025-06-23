package com.project.tracker.services;

import com.project.tracker.exceptions.customExceptions.UserNotFoundException;
import com.project.tracker.models.Users;
import com.project.tracker.repositories.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UsersDetailServiceImpl implements UserDetailsService {
    private final UsersRepository usersRepository;

    public UsersDetailServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //retrieve user obj from database
        Users user = usersRepository.findByEmail(username);

        if(user == null){
            throw new UserNotFoundException("User not found");
        }
        return new UsersDetailImpl(user);
    }
}
