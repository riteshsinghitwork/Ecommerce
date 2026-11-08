package com.ecommerce.project.payload;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long cartId;
    private CartDTO cartDTO;
    private ProductDTO productDTO;
    private Integer quantity;
    private Double discount;
    private Double productPrice;
}


