package com.nbu.logistics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipmentCreateRequest {

    @NotNull
    private Double weight;

    @NotBlank
    private String receiverName;

    private String receiverPhone;

    @NotNull
    private Integer deliveryTypeId;

    @NotNull
    private Integer clientAddressId;

    @NotNull
    private Integer officeAddressId;
}