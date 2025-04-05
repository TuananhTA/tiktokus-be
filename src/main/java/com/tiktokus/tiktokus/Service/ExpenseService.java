package com.tiktokus.tiktokus.Service;

import com.tiktokus.tiktokus.Entity.Expense;
import com.tiktokus.tiktokus.Entity.Orders;
import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;



    public Expense create(Expense e){
        return expenseRepository.save(e);
    }

    public Expense get() {
        return expenseRepository.findById(1L)
                .orElseGet(() -> {
                    Expense newExpense = new Expense(0,1,0);
                    return expenseRepository.save(newExpense);
                });
    }
    public Expense update(Expense e){
        Expense expense = get();
        expense.setExpense(e.getExpense());
        expense.setExpense(e.getExpense());
        return expenseRepository.save(e);
    }

    public boolean isOfExpense(){
        User u  = userService.getUseLogin();
        Expense expense = expenseRepository.findById(1L)
                .orElse(null);
        return expense != null;
    }
}
