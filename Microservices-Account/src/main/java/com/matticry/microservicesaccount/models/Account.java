package com.matticry.microservicesaccount.models;

import com.matticry.microservicesaccount.enums.TypeAccount;
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
@Table(name = "tbl_accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account")
    private Long idAccount;

    @Column(name = "number_account", length = 10)
    private String numberAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_account")
    private TypeAccount typeAccount;

    @Column(name = "date_account")
    private LocalDateTime dateAccount;

    @Column(name = "initial_balance_acc", precision = 10, scale = 2)
    private BigDecimal initialBalanceAcc;

    @Column(name = "status_account", length = 1)
    private Character statusAccount;

    @Column(name = "client_id")
    private Long clientId;
}
