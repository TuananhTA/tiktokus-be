package com.tiktokus.tiktokus.Restcontroller;

import com.tiktokus.tiktokus.DTO.ApiResponse;
import com.tiktokus.tiktokus.Entity.Transaction;
import com.tiktokus.tiktokus.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/private/transaction")
public class TransactionRest {
    
    @Autowired
    TransactionService transactionService;

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<Transaction>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        try {
            return ResponseEntity.ok(ApiResponse.success(transactionService.getAll(page,size),"Transaction found successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @GetMapping("/get-user-login")
    public ResponseEntity<ApiResponse<Page<Transaction>>> getByUserLogin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        try {
            return ResponseEntity.ok(ApiResponse.success(transactionService.getByUserLogin(page,size),"Transaction found successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }
}
