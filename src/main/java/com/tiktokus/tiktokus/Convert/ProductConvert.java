package com.tiktokus.tiktokus.Convert;

import com.tiktokus.tiktokus.DTO.ProductDTO;
import com.tiktokus.tiktokus.Entity.OrderDetails;
import com.tiktokus.tiktokus.Entity.Product;
import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Enum.OrderStatus;
import com.tiktokus.tiktokus.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ProductConvert {
    public static ProductDTO convertToDTO(Product p){
        long sum = 0;
        if( p.getOrderDetailsList()!= null && !p.getOrderDetailsList().isEmpty()){
            sum = p.getOrderDetailsList()
                    .stream()
                    .filter(item -> item.getOrders().getStatus() != OrderStatus.CANCELED)
                    .mapToLong(OrderDetails::getQuantity)
                    .sum();
        }
        return ProductDTO.builder()
                .id(p.getId())
                .sku(p.getSku())
                .status(p.getStatus())
                .productName(p.getProductName())
                .createdAt(p.getCreatedAt())
                .price(p.getPrice())
                .quantity(p.getQuantity())
                .quantityInOrder(sum)
                .updatedAt(p.getUpdatedAt())
                .urlImage(p.getUrlImage())
                .build();
    }

    public static List<ProductDTO> convertListToDTO(List<Product> list){
        return list.stream()
                .map(ProductConvert::convertToDTO)
                .collect(Collectors.toList());
    }

    public static Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setUrlImage(productDTO.getUrlImage());

        return product;
    }

}
