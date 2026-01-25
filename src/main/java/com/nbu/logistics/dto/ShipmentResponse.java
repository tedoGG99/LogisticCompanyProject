package com.nbu.logistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShipmentResponse {

    private Integer id;
    private Integer trackingNumber;
    private Double weight;
    private String receiverName;
    private String status;
}
