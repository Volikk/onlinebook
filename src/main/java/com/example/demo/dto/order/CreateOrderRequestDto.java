package com.example.demo.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequestDto {
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;
}
