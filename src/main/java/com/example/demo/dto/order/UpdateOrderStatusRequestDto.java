package com.example.demo.dto.order;

import com.example.demo.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequestDto {
    @NotNull(message = "Status is required")
    private OrderStatus status;
}
