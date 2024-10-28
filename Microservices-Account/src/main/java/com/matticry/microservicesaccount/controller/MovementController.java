package com.matticry.microservicesaccount.controller;

import com.matticry.microservicesaccount.dto.MovementReportDTO;
import com.matticry.microservicesaccount.dto.MovementRequestDTO;
import com.matticry.microservicesaccount.dto.MovementResponseDTO;
import com.matticry.microservicesaccount.service.MovementServiceImpl;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
@Slf4j  // Añade esta anotación

public class MovementController {
    private final MovementServiceImpl movementService;

    @PostMapping
    public Single<ResponseEntity<MovementResponseDTO>> createMovement(@RequestBody MovementRequestDTO requestDTO) {
        return movementService.createMovement(requestDTO)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }
    @GetMapping("/report")
    public Single<ResponseEntity<List<MovementReportDTO>>> getMovementReport(
            @RequestParam(required = true) String startdate,
            @RequestParam(required = true) String enddate,
            @RequestParam(required = true) String clientName) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate parsedStartDate = LocalDate.parse(startdate, formatter);
            LocalDate parsedEndDate = LocalDate.parse(enddate, formatter);

            return movementService.getMovementForDateAndClient(parsedStartDate, parsedEndDate, clientName)
                    .map(ResponseEntity::ok)
                    .doOnError(error -> log.error("Error getting report: ", error));
        } catch (DateTimeParseException e) {
            return Single.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid date format. Use dd/MM/yyyy"
            ));
        }
    }
}
