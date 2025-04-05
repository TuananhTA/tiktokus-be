package com.tiktokus.tiktokus.Service;

import com.tiktokus.tiktokus.Convert.UserConvert;
import com.tiktokus.tiktokus.DTO.TokenResponse;
import com.tiktokus.tiktokus.DTO.UserLogin;
import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {


    @Autowired
    UserRepository accountRepository;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoderService passwordEncoderService;


    @Autowired
    JWTService jwtService;

    public TokenResponse authentication(UserLogin accountRequest){
        User account = accountRepository
                .findByEmail(accountRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Not found account!"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(accountRequest.getEmail(),accountRequest.getPassword())
        );
        return TokenResponse.builder()
                .accessToken(jwtService.generateToken(account))
                .resfreshToken("1234")
                .userDTO(UserConvert.convertDTO(account))
                .build();

    }
}
