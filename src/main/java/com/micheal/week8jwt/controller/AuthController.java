package com.micheal.week8jwt.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.micheal.week8jwt.dto.UserDto;
import com.micheal.week8jwt.model.Users;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.micheal.week8jwt.serviceimpl.UserServiceImpl;
import com.micheal.week8jwt.Util.JwtUtils;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class AuthController  {

    private UserServiceImpl userService;
    private JwtUtils jwtUtils;
//    private GoogleJwtUtils googleJwtUtils;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserServiceImpl userService, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;

        this.passwordEncoder = passwordEncoder;
    }

//    @GetMapping("/google/{tkn}")
//    public ResponseEntity<String> authorizeOauthUser(@PathVariable("tkn") String token){
//        return ResponseEntity.ok(googleJwtUtils.googleOauthUserJWT(token));
//
//    }

    @GetMapping("/index")
    @SecurityRequirement(name = "Bearer Authentication")
    public String index(){
        return "index";
    }
    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUpUser(@RequestBody UserDto userDto){
        Users user = userService.saveUser.apply(userDto);
        UserDto userDto1 = new ObjectMapper().convertValue(user, UserDto.class);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto){
        UserDetails user = userService.loadUserByUsername(userDto.getUsername());
        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())){
            String token = jwtUtils.createJwt.apply(user);
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Username or Password not correct!",
                HttpStatus.BAD_REQUEST);
    }

}