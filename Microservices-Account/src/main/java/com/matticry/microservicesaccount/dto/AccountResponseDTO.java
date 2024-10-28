package com.matticry.microservicesaccount.dto;

import com.matticry.microservicesaccount.enums.TypeAccount;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    private Long idAccount;
    private String numberAccount;
    private String typeAccount;
    private LocalDateTime dateAccount;
    private BigDecimal initialBalanceAcc;
    private Character statusAccount;
    private ClientDTO client;
    private List<MovementResponseDTO> movements;
}
