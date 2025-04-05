package com.tiktokus.tiktokus.DTO;

import com.tiktokus.tiktokus.Enum.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private Date updatedAt;
    private Date createdAt;

    @NotBlank(message = "Product name cannot be blank.")
    private String productName;
    private String sku;

    @Min(value = 0, message = "Quantity must be greater than or equal to 0.")
    private long quantity;

    private long quantityInOrder;

    @Min(value = 0, message = "Quantity must be greater than or equal to 0.")
    private float price ;
    private String urlImage;
    private ProductStatus status;
}
