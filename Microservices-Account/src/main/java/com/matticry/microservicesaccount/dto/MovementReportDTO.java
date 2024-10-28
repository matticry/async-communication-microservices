package com.matticry.microservicesaccount.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovementReportDTO {

   private String  date;
   private String client;
   private String numberAccount;
   private String typeAccount;
   private BigDecimal initialBalanceAcc;
   private Character statusAccount;
   private BigDecimal value;
   private BigDecimal balanceMov;
   private String transactionType;
}
