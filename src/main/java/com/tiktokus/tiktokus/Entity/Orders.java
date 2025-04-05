package com.tiktokus.tiktokus.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tiktokus.tiktokus.Enum.OrderStatus;
import com.tiktokus.tiktokus.Enum.OrderType;
import com.tiktokus.tiktokus.Enum.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Orders extends Base {

    private String customName;

    @Column(unique = true, nullable = false)
    private String orderCode;

    private String phone;

    @Column
    private String email;

    private String street;

    private String streetTwo;

    private String country;

    private String zipCode;

    @Column
    private long quantity;

    @Column
    private float total;

    @Column
    private float expense;

    @Column
    private float expenseLable;

    @Column
    private String labelUrl;
    @Column
    private String tracking;
    @Column
    private String extraId;
    @Column
    private long groupId;
    @Column
    private float preProduct;
    @Column
    private String state;

    @Column
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OrderType type;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<OrderDetails> orderDetailsList;

    @Column
    private String desgin;

    @Column
    private String mockup;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("user")
    User user;

    public void generateOrderCode() {
        this.orderCode = System.currentTimeMillis()  + UUID.randomUUID().toString().substring(0, 5);
    }

}
