package com.tiktokus.tiktokus.Restcontroller;

import com.tiktokus.tiktokus.DTO.ApiResponse;
import com.tiktokus.tiktokus.Entity.Expense;
import com.tiktokus.tiktokus.Service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private/expense")
public class ExpenseRest {

    @Autowired
    ExpenseService expenseService;

    @GetMapping("/get-expense")
    public ResponseEntity<ApiResponse<Expense>> get(){
        try{
            return ResponseEntity.ok(ApiResponse.success(expenseService.get(), "Get expense successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(HttpStatus.CONFLICT.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Expense>> update(@RequestBody Expense expense){
        try{
            return ResponseEntity.ok(ApiResponse.success(expenseService.update(expense), "Update expense successfully"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(HttpStatus.CONFLICT.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred"));
        }
    }
}
