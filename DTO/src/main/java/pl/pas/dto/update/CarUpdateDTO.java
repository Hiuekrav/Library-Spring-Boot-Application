package pl.pas.dto.update;

import lombok.Builder;
import java.util.UUID;

@Builder
public record CarUpdateDTO(
        UUID id,
        String plateNumber,
        Double basePrice,
        Integer engineDisplacement,
        String transmissionType,
        boolean archive
){}
