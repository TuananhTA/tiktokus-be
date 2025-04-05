package com.tiktokus.tiktokus.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Category extends Base{

    @Column
    private String categoryName;

    @ManyToMany
    @JoinTable(
            name = "category_product",
            joinColumns = @JoinColumn(name = "category_id"), // Khóa ngoại đến Product
            inverseJoinColumns = @JoinColumn(name = "product_id") // Khóa ngoại đến Category
    )
    private Set<Product> products = new HashSet<>();


}
