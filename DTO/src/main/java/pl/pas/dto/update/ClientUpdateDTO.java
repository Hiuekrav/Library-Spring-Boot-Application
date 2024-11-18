package pl.pas.dto.update;

import lombok.Builder;

import java.util.UUID;
@Builder
public record ClientUpdateDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        UUID clientTypeId,
        String cityName,
        String streetName,
        String streetNumber
){}
