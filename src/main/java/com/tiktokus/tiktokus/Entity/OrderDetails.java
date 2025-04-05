package com.tiktokus.tiktokus.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetails extends Base{


    @Column
    private Long quantity;
    @Column
    private float price;
    @Column
    private float total;

    @Column
    private float curPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties("orderDetailsList")
    Product product;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    @JsonBackReference
    Orders orders;

    public OrderDetails clone() {
        OrderDetails clone = new OrderDetails();
        clone.setProduct(this.product); // Nếu bạn cũng muốn sao chép product, hãy đảm bảo tạo bản sao của nó nếu cần
        clone.setQuantity(this.quantity);
        clone.setCurPrice(this.curPrice);
        clone.setTotal(this.total);
        return clone;
    }
}
