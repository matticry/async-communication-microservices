package com.matticry.microservicesproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovementDTO {
    private Long idMov;
    private LocalDateTime dateMov;
    private String transactionTypeMov;
    private BigDecimal valueMov;
    private BigDecimal balanceMov;
}
