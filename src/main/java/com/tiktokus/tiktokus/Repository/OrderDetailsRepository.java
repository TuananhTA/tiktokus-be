package com.tiktokus.tiktokus.Repository;

import com.tiktokus.tiktokus.Entity.OrderDetails;
import com.tiktokus.tiktokus.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long> {
}
