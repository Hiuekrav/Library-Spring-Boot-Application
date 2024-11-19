package pl.pas.dto.create;

public record CarCreateDTO(
        String plateNumber,
        Double basePrice,
        Integer engineDisplacement,
        String transmissionType
){}
