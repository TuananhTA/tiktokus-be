package com.tiktokus.tiktokus.Restcontroller;


import com.tiktokus.tiktokus.DTO.ApiResponse;
import com.tiktokus.tiktokus.DTO.UserDTO;
import com.tiktokus.tiktokus.DTO.UserLogin;
import com.tiktokus.tiktokus.Entity.ExcelFile;
import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Repository.ExcelFileRepository;
import com.tiktokus.tiktokus.Service.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class AuthRest {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    CategoryService categoryService;

    @Autowired
    private ExcelFileRepository uploadedFileRepository;

    @Autowired
    UserService userService;

    @Autowired
    TelegramService telegrambot;

    @GetMapping("/sendMess")
    public String sendMess() {
        try {
            telegrambot.sendMessage("Login");
            return "Succ";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginHandle(@RequestBody UserLogin accountRequest, HttpServletResponse response){

        try{
            return ResponseEntity.ok(ApiResponse.success(authenticationService.authentication(accountRequest), "Login successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody User user){
        try {
            return ResponseEntity.ok(ApiResponse.success(userService.register(user), "Register successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Users not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadXlsx() {
        List<ExcelFile> file = uploadedFileRepository.findAll();
        if(file.isEmpty()) return ResponseEntity.badRequest().body("Not found file!");

        // Giả sử file.get(0).getFileData() là byte array của file XLSX
        byte[] fileData = file.get(0).getFileData();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"template.xlsx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(fileData);
    }

    @GetMapping("/get-all-category")
    public ResponseEntity<?> getAllCategory(){
        try {
            return ResponseEntity.ok(ApiResponse.success(categoryService.getAll(), "Lấy danh mục thành công!"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }


}
