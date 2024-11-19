package pl.pas.dto.update;

import lombok.Builder;

import java.util.UUID;
@Builder
public record UserUpdateDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String cityName,
        String streetName,
        String streetNumber
){}
