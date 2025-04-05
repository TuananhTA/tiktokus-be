package com.tiktokus.tiktokus.Specification;

import com.tiktokus.tiktokus.Entity.Category;
import com.tiktokus.tiktokus.Entity.Product;
import com.tiktokus.tiktokus.Entity.UserProduct;
import com.tiktokus.tiktokus.Enum.ProductStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {

    public static Specification<Product> hasSkus(List<String> skus) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            return root.get("sku").in(skus);
        };
    }

    public static Specification<Product> hasProductName(String productName) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (productName == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("productName"), productName);
        };
    }

    public static Specification<Product> hasStatus(ProductStatus status) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            return criteriaBuilder.equal(root.get("status"), status);
        };
    }




    public static Specification<Product> hasUser(Long userId) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, UserProduct> userProductJoin = root.join("userProductList");
            return criteriaBuilder.equal(userProductJoin.get("user").get("id"), userId);
        };
    }

    public static Specification<Product> hasNameOrSku(String nameOrSku) {
        return (root, query, criteriaBuilder) -> {
            if (nameOrSku == null || nameOrSku.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("productName"), "%" + nameOrSku + "%"),
                    criteriaBuilder.like(root.get("sku"), "%" + nameOrSku + "%")
            );
        };
    }

    // Tìm theo tên category
    public static Specification<Product> hasCategoryName(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Product, Category> categoryJoin = root.join("categories"); // Giả sử `Product` có quan hệ với `Category`
            return criteriaBuilder.like(categoryJoin.get("categoryName"), "%" + categoryName + "%");
        };
    }

    public static Specification<Product> hasStatuses(List<ProductStatus> statuses) {
        return (root, query, criteriaBuilder) -> {
            if (statuses == null || statuses.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("status").in(statuses);
        };
    }

    public static Specification<Product> searchProducts(String nameOrSku, String categoryName, List<ProductStatus> statuses) {
        return Specification
                .where(hasNameOrSku(nameOrSku))
                .and(hasCategoryName(categoryName))
                .and(hasStatuses(statuses));
    }

}