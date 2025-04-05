package com.tiktokus.tiktokus.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiktokus.tiktokus.Enum.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date createdAt;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;

    @Column
    private String productName;
    @Column
    private String sku;
    @Column
    private long quantity;
    @Column
    private float price ;
    @Column
    private String urlImage;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    List<UserProduct> userProductList;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @Column
    private Long groupId;

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private Set<Category> categories = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderDetails> orderDetailsList;

    public static String renSKU() {
        return "SKU-" + UUID.randomUUID().toString();
    }

    @PrePersist
    public void setCreationDate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.status = ProductStatus.AVAILABLE;
        if(this.quantity == 0){
            this.status = ProductStatus.DISCONNECT;
        }
    }

    @PreUpdate
    public void setChangeDate() {
        this.updatedAt = new Date();
        if(this.quantity == 0){
            this.status = ProductStatus.DISCONNECT;
        }
    }

}
