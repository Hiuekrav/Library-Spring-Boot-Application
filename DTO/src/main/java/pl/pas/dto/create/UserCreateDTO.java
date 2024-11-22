package pl.pas.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import pl.pas.dto.ValidationConstants;

public record UserCreateDTO(

        @NotBlank(message = ValidationConstants.FIRST_NAME_BLANK)
        @Size(min = ValidationConstants.FIRST_NAME_MIN_LENGTH, message = ValidationConstants.FIRST_NAME_TOO_SHORT)
        @Size(max = ValidationConstants.FIRST_NAME_MAX_LENGTH, message = ValidationConstants.FIRST_NAME_TOO_LONG)
        String firstName,

        @NotBlank(message = ValidationConstants.LAST_NAME_BLANK)
        @Size(min = ValidationConstants.LAST_NAME_MIN_LENGTH, message = ValidationConstants.LAST_NAME_TOO_SHORT)
        @Size(max = ValidationConstants.LAST_NAME_MAX_LENGTH, message = ValidationConstants.LAST_NAME_TOO_LONG)
        String lastName,

        @NotBlank(message = ValidationConstants.EMAIL_BLANK)
        @Email(message = ValidationConstants.EMAIL_INVALID_FORMAT)
        String email,

        @NotBlank(message = ValidationConstants.PASSWORD_BLANK)
        @Size(min = ValidationConstants.PASSWORD_MIN_LENGTH, message = ValidationConstants.PASSWORD_TOO_SHORT)
        String password,

        @NotBlank(message = ValidationConstants.CITY_NAME_BLANK)
        String cityName,

        @NotBlank(message = ValidationConstants.STREET_NAME_BLANK)
        String streetName,

        @NotBlank(message = ValidationConstants.STREET_NUMBER_BLANK)
        String streetNumber
){}
