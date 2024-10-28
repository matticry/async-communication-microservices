package com.matticry.microservicesaccount.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementReportRequestDTO {
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime date;

    private String client;
}
