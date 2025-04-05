package com.tiktokus.tiktokus.Repository;

import com.tiktokus.tiktokus.Entity.Orders;
import com.tiktokus.tiktokus.Entity.Product;
import com.tiktokus.tiktokus.Enum.OrderStatus;
import com.tiktokus.tiktokus.Enum.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders,Long>, JpaSpecificationExecutor<Orders> {

    List<Orders> findAllByStatus(OrderStatus status);

    Page<Orders> findAll(Pageable pageable);

    List<Orders> findAll();

    @Query("SELECT o FROM Orders o WHERE o.groupId = ?1 ORDER BY o.createdAt DESC")
    Page<Orders> findByGroupId(Long groupId, Pageable pageable);

    Optional<Orders> findByIdAndGroupId(Long id, Long groupId);
}
