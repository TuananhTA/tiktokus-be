package com.tiktokus.tiktokus.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserProduct extends Base{


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("userProductList")
    User user;


    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties("userProductList")

    Product product;

}
