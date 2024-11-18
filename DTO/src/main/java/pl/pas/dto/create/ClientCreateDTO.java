package pl.pas.dto.create;

import java.util.UUID;

public record ClientCreateDTO(
        String firstName,
        String lastName,
        String email,
        UUID clientTypeId,
        String cityName,
        String streetName,
        String streetNumber
){}
