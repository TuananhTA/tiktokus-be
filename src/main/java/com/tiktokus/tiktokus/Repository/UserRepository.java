package com.tiktokus.tiktokus.Repository;

import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Enum.ProductStatus;
import com.tiktokus.tiktokus.Enum.RoleUser;
import com.tiktokus.tiktokus.Enum.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);

    List<User> findByStatusInAndRoleIn(List<UserStatus> userStatuses, List<RoleUser> roles);
}