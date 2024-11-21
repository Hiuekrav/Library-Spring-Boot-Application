package pl.pas.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pas.dto.ValidationConstants;

public record UserCreateDTO(

        @Size(min = ValidationConstants.FIRST_NAME_MIN_LENGTH, message = ValidationConstants.FIRST_NAME_TOO_SHORT)
        @Size(max = ValidationConstants.FIRST_NAME_MAX_LENGTH, message = ValidationConstants.FIRST_NAME_TOO_LONG)
        String firstName,
        String lastName,

        @NotBlank(message = ValidationConstants.EMAIL_BLANK)
        @Email(message = ValidationConstants.EMAIL_INVALID_FORMAT)
        String email,
        String password,
        String cityName,
        String streetName,
        String streetNumber
){}
