package pl.pas.dto.output;

import java.time.LocalDateTime;
import java.util.UUID;

public record RentOutputDTO(
        UUID id,
        UserOutputDTO userOutputDTO,
        CarOutputDTO carOutputDTO,
        LocalDateTime beginTime,
        LocalDateTime endTime,
        Double rentCost,
        boolean active
){}
