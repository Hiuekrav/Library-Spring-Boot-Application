package pl.pas.rest.model.users;

import lombok.Getter;
import lombok.Setter;
import pl.pas.rest.mgd.users.AdminMgd;
import pl.pas.rest.mgd.users.UserMgd;

import java.util.UUID;

@Getter @Setter
public class Admin extends User {

    public Admin(UUID id, String firstName, String lastName, String email, String password,
                 String cityName, String streetName, String streetNumber) {
        super(id, firstName, lastName, email, password, cityName, streetName, streetNumber);
    }

    public Admin(AdminMgd adminMgd) {
        super(adminMgd);
    }

}
