package pl.pas.dto.create;

public record UserCreateDTO(
        String firstName,
        String lastName,
        String email,
        String password,
        String cityName,
        String streetName,
        String streetNumber
){}
