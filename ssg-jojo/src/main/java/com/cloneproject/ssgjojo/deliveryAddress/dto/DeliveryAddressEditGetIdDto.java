package com.cloneproject.ssgjojo.deliveryAddress.dto;

import com.cloneproject.ssgjojo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressEditGetIdDto {
    private Long id;
    private String address; // 배송지 (배송지 수정)
    private boolean whetherDefaultAddress; // 기본배송지 여부 (배송지 수정)

    private Long user;
}
