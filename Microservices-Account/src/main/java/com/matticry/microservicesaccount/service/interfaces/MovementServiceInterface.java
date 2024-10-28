package com.matticry.microservicesaccount.service.interfaces;

import com.matticry.microservicesaccount.dto.MovementRequestDTO;
import com.matticry.microservicesaccount.dto.MovementResponseDTO;
import com.matticry.microservicesaccount.models.Movement;
import io.reactivex.rxjava3.core.Single;

import java.time.LocalDateTime;
import java.util.List;

public interface MovementServiceInterface {
    Single<MovementResponseDTO> createMovement(MovementRequestDTO movementRequestDTO);

}
