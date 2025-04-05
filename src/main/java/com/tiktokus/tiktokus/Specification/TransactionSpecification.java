package com.tiktokus.tiktokus.Specification;

import com.tiktokus.tiktokus.Entity.Product;
import com.tiktokus.tiktokus.Entity.Transaction;
import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Entity.UserProduct;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecification {

    public static Specification<Transaction> hasUser(Long userId) {
        return (root, query, criteriaBuilder) -> {
            Join<Transaction, User> userJoin = root.join("user");
            return criteriaBuilder.equal(userJoin.get("id"), userId);
        };
    }

}
