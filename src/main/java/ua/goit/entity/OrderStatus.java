package ua.goit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PLACED("placed"),APPROVED("approved"),DELIVERED("delivered");
    private final String status;

    public static Optional<OrderStatus> getOrderStatus(String status){
        return Arrays.stream(OrderStatus.values())
                .filter(enumValue->enumValue.getStatus().equals(status))
                .findAny();
    }



}

