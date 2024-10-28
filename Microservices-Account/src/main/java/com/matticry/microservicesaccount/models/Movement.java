package com.matticry.microservicesaccount.models;

import com.matticry.microservicesaccount.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tbl_movements")
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mov")
    private Long idMov;

    @Column(name = "date_mov")
    private LocalDateTime dateMov;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type_mov")
    private TransactionType transactionTypeMov;

    @Column(name = "value_mov", precision = 10, scale = 2)
    private BigDecimal valueMov;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "balance_mov", precision = 10, scale = 2)
    private BigDecimal balanceMov;
}
