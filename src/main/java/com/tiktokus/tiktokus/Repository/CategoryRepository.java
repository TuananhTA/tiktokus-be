package com.tiktokus.tiktokus.Repository;

import com.tiktokus.tiktokus.Entity.Category;
import com.tiktokus.tiktokus.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String name);

    @Query("SELECT DISTINCT c FROM Category c JOIN c.products p WHERE p IN :products")
    HashSet<Category> findDistinctCategoriesByProducts(@Param("products") List<Product> products);

}
