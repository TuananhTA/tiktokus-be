package com.tiktokus.tiktokus.Specification;

import com.tiktokus.tiktokus.Entity.Orders;
import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Enum.OrderStatus;
import com.tiktokus.tiktokus.Enum.RoleUser;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderSpecification {

    public static Specification<Orders> hasOrderId(Long orderId){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("id"),orderId);
        };
    }

    public static Specification<Orders> hasExtraId(String extraId){
        return (root, query, criteriaBuilder) -> {
            if (extraId == null || extraId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("extraId"),extraId );
        };
    }

    public static Specification<Orders> hasStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Orders> hasPhone(String phone) {
        return (root, query, criteriaBuilder) -> {
            if (phone == null || phone.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("phone"), "%" + phone);
        };
    }
    public static Specification<Orders> hasCustomerName(String customerName) {
        return (root, query, criteriaBuilder) -> {
            if (customerName == null || customerName.isEmpty()) {
                return criteriaBuilder.conjunction(); // Không thêm điều kiện nếu customerName là null hoặc rỗng
            }
            return criteriaBuilder.like(root.get("customName"), "%" + customerName + "%");
        };
    }

    public static Specification<Orders> hasGroupId(Long groupId) {
        return (root, query, criteriaBuilder) -> {
            if (groupId == null || groupId == 0) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("groupId"),groupId);
        };
    }

    public static Specification<Orders> hasUserRole(RoleUser role) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("role"), role);
    }


    public static Specification<Orders> hasDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            // Kiểm tra nếu cả hai ngày bắt đầu và kết thúc không phải là null
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();  // Điều kiện luôn đúng nếu không có ngày
            }
            // Nếu chỉ có startDate
            if (startDate != null && endDate == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate);
            }
            // Nếu chỉ có endDate
            if (startDate == null && endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
            }
            // Nếu có cả hai ngày bắt đầu và kết thúc
            return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
        };
    }

}
