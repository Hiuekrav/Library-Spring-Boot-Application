package pl.pas.dto.output;

import java.util.UUID;

public record CarOutputDTO (
        UUID id,
        String plateNumber,
        Double basePrice,
        Integer engineDisplacement,
        String transmissionType,
        boolean archive
){}
