package com.tiktokus.tiktokus.Repository;

import com.tiktokus.tiktokus.Entity.Product;
import com.tiktokus.tiktokus.Enum.OrderStatus;
import com.tiktokus.tiktokus.Enum.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p WHERE (LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR p.sku =:keyword) " +
            "AND p.groupId = :groupId AND p.status IN :statuses")
    List<Product> searchProducts(@Param("keyword") String keyword,
                                 @Param("groupId") Long groupId,
                                 @Param("statuses") List<ProductStatus> statuses);;

    @Query("SELECT up.product FROM UserProduct up WHERE up.user.id = :userId AND up.product.status IN :statuses ORDER BY up.createdAt DESC")

    Page<Product> findProductsByUserIdAndProductStatuses(
            @Param("userId") Long userId,
            @Param("statuses") List<ProductStatus> statuses,
            Pageable pageable
    );

    @Query("SELECT up.product FROM UserProduct up WHERE up.user.id = :userId AND up.product.status IN :statuses")
    List<Product> findProductsByUserIdAndProductStatuses(
            @Param("userId") Long userId,
            @Param("statuses") List<ProductStatus> statuses
    );

    @Query("SELECT up.product FROM UserProduct up WHERE up.user.id = :userId AND " +
            "(up.product.productName LIKE %:searchTerm% OR up.product.sku = :searchTerm) AND " +
            "up.product.status IN :statuses")
    Page<Product> findProductsByUserIdAndProductNameOrSkuAndStatuses(@Param("userId") Long userId,
                                                                     @Param("searchTerm") String searchTerm,
                                                                     @Param("statuses") List<ProductStatus> statuses,
                                                                     Pageable pageable
    );

    @Query("SELECT up.product FROM UserProduct up WHERE up.user.id = :userId AND " +
            "(up.product.productName LIKE %:searchTerm% OR up.product.sku = :searchTerm) AND " +
            "up.product.status IN :statuses")
    List<Product> findProductsByUserIdAndProductNameOrSkuAndStatuses(@Param("userId") Long userId,
                                                                     @Param("searchTerm") String searchTerm,
                                                                     @Param("statuses") List<ProductStatus> statuses
    );
    @Query("SELECT up.product FROM UserProduct up WHERE " +
            "(up.product.productName LIKE %:searchTerm% OR up.product.sku = :searchTerm) AND " +
            "up.product.status IN :statuses")
    List<Product> findProductsByProductNameOrSkuAndStatuses(@Param("searchTerm") String searchTerm,
                                                            @Param("statuses") List<ProductStatus> statuses
    );

    Page<Product> findByStatusIn(List<ProductStatus> statuses, Pageable pageable);

    List<Product> findByStatusIn(List<ProductStatus> statuses);
    Optional<Product> findBySku(String sku);
}
