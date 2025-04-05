package com.tiktokus.tiktokus.Restcontroller;

import com.tiktokus.tiktokus.Convert.UserConvert;
import com.tiktokus.tiktokus.DTO.ApiResponse;
import com.tiktokus.tiktokus.DTO.UserDTO;
import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Enum.RoleUser;
import com.tiktokus.tiktokus.Enum.UserStatus;
import com.tiktokus.tiktokus.Service.UserService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/private/user")
@Slf4j
public class UserRest {

    @Autowired
    UserService userService;

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<UserDTO>>> listAll(){
        try {
            return ResponseEntity.ok(ApiResponse.success(userService.getAll(), "Users found successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Users not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @GetMapping("/get-staff")
    public ResponseEntity<ApiResponse<List<UserDTO>>> listAllStatus(){
        try {
            return ResponseEntity.ok(ApiResponse.success(userService.getAllBStatus(List.of(UserStatus.ACTIVE, UserStatus.LOCKED)), "Users found successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Users not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @GetMapping("/get-user-login")
    public ResponseEntity<ApiResponse<UserDTO>> getUserLogin(){
        try {
            return ResponseEntity.ok(ApiResponse.success(UserConvert.convertDTO(userService.getUseLogin()), "Users found successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Users not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> create(@RequestBody User user){
        try {
            return ResponseEntity.ok(ApiResponse.success(userService.create(user), "Users create successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(HttpStatus.CONFLICT.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @PutMapping("/edit/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> edit(@PathVariable("userId") long id ,@RequestBody User user){
        try {
            return ResponseEntity.ok(ApiResponse.success(userService.edit(user, id), "User update successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(HttpStatus.CONFLICT.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }
    @GetMapping("/get-role")
    public ResponseEntity<?> getALlRoles(){
        try {
            List<RoleUser> roles = new ArrayList<>(List.of(RoleUser.values()));
            roles.remove(RoleUser.ADMIN);

            return ResponseEntity.ok(ApiResponse.success(roles, "User update successfully"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

}
