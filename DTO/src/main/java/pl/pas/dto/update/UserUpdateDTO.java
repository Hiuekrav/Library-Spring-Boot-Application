package pl.pas.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import pl.pas.dto.ValidationConstants;

import java.util.UUID;
@Builder
public record UserUpdateDTO(

        @NotNull(message = ValidationConstants.USER_UUID_BLANK)
        UUID id,

        @Size(min = ValidationConstants.FIRST_NAME_MIN_LENGTH, message = ValidationConstants.FIRST_NAME_TOO_SHORT)
        @Size(max = ValidationConstants.FIRST_NAME_MAX_LENGTH, message = ValidationConstants.FIRST_NAME_TOO_LONG)
        String firstName,

        @Size(min = ValidationConstants.LAST_NAME_MIN_LENGTH, message = ValidationConstants.LAST_NAME_TOO_SHORT)
        @Size(max = ValidationConstants.LAST_NAME_MAX_LENGTH, message = ValidationConstants.LAST_NAME_TOO_LONG)
        String lastName,

        @NotBlank(message = ValidationConstants.EMAIL_BLANK)
        @Email(message = ValidationConstants.EMAIL_INVALID_FORMAT)
        String email,

        String cityName,
        String streetName,
        String streetNumber
){}
