package com.tiktokus.tiktokus.Repository;

import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Entity.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserProductRepository extends JpaRepository<UserProduct, Long> {

    List<UserProduct> findByUser(User user);
    @Transactional
    void deleteByUser(User user);
}
