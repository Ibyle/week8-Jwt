package com.micheal.week8jwt.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micheal.week8jwt.dto.UserDto;
import com.micheal.week8jwt.enums.Role;
import com.micheal.week8jwt.model.Users;
import com.micheal.week8jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserServiceImpl implements UserDetailsService {


    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Function<UserDto, Users> saveUser= (userDto)->{
        Users user = new ObjectMapper().convertValue(userDto, Users.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(Role.ROLE_USER);
        return userRepository.save(user);
    };

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found with username: "+username));
    }
}
