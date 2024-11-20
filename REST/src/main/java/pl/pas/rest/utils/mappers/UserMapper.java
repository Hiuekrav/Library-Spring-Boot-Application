package pl.pas.rest.utils.mappers;

import pl.pas.dto.output.UserDataOutputDTO;
import pl.pas.dto.output.UserOutputDTO;
import pl.pas.rest.model.users.User;

public class UserMapper {

    public static UserOutputDTO toUserOutputDTO(User user) {
        return new UserOutputDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCityName(),
                user.getStreetName(),
                user.getStreetNumber()
        );
    }

    public static UserDataOutputDTO toUserDataOutputDTO(User user) {
        return new UserDataOutputDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
