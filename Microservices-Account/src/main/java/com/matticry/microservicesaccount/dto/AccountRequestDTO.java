package com.matticry.microservicesaccount.dto;

import com.matticry.microservicesaccount.enums.TypeAccount;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Data
@Getter
@Setter
@NoArgsConstructor
public class AccountRequestDTO {
    private String numberAccount;
    private TypeAccount typeAccount;
    private BigDecimal initialBalanceAcc;
    private Long clientId;
}
