package com.tiktokus.tiktokus.Service;


import com.tiktokus.tiktokus.Entity.Transaction;
import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Enum.TransactionType;
import com.tiktokus.tiktokus.Repository.TransactionRepository;
import com.tiktokus.tiktokus.Specification.TransactionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Lazy
    @Autowired
    UserService userService;

    @Transactional
    public Transaction create(Long userId,float amount, float preBalance, String description){
        User userFind = userService.getById(userId);
        User userlogin  = userService.getUseLogin();
        System.out.println(description);
        if(amount == 0) {
            System.out.println("Không thay đổi");
            return null;
        }
        TransactionType type  = amount > 0 ?
                TransactionType.PLUS :  TransactionType.MINUS;
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .description(description)
                .prevBalance(preBalance)
                .user(userFind)
                .type(type)
                .build();
        transaction.setUpdatedBy(userlogin.getFullName());
        return transactionRepository.save(transaction);
    }

    public Page<Transaction> getAll(int page, int  size){
        Sort sort =  Sort.by(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page,size, sort);
        return transactionRepository.findAll(pageable);
    }

    public Page<Transaction> getByUserLogin(int page, int  size){
        Sort sort =  Sort.by(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page,size, sort);
        User user = userService.getUseLogin();
        Specification<Transaction> spec = TransactionSpecification.hasUser(user.getId());
        return transactionRepository.findAll(spec,pageable);
    }
}
