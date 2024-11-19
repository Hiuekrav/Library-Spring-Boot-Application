package pl.pas.dto.output;

import java.util.UUID;

public record UserDataOutputDTO(
        UUID id,
        String firstName,
        String lastName,
        String email
){}
