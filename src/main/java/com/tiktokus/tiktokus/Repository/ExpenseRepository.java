package com.tiktokus.tiktokus.Repository;

import com.tiktokus.tiktokus.Entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {


}
