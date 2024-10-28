package com.matticry.microservicesproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
    private Long idAccount;
    private String numberAccount;
    private String typeAccount;
    private LocalDateTime dateAccount;
    private BigDecimal initialBalanceAcc;
    private Character statusAccount;
    private List<MovementDTO> movements;
}
