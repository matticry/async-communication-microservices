package com.matticry.microservicesaccount.dto;

import com.matticry.microservicesaccount.enums.TransactionType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
public class MovementRequestDTO {
    private Integer accountId;
    private TransactionType transactionType;
    private BigDecimal value;
}
