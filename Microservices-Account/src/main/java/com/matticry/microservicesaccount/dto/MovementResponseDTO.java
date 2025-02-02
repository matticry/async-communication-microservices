package com.matticry.microservicesaccount.dto;

import com.matticry.microservicesaccount.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementResponseDTO {

    private Long idMov;
    private LocalDateTime dateMov;
    private TransactionType transactionTypeMov;
    private BigDecimal valueMov;
    private BigDecimal balanceMov;
    private AccountResponseDTO account;
}
