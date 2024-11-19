package pl.pas.dto.output;

import java.util.UUID;

public record UserOutputDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String cityName,
        String streetName,
        String streetNumber
){}
