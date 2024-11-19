package pl.pas.rest.model.users;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.model.AbstractEntity;
import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Getter @Setter
public class User extends AbstractEntity {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String cityName;
    private String streetName;
    private String streetNumber;
    private boolean active;

    public User(UUID id, String firstName, String lastName, String email, String password,
                String cityName, String streetName, String streetNumber) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.cityName = cityName;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.active = true;
    }


    public User(UserMgd userMgd) {
        super(userMgd.getId());
        this.firstName = userMgd.getFirstName();
        this.lastName = userMgd.getLastName();
        this.email = userMgd.getEmail();
        this.password = userMgd.getPassword();
        this.cityName = userMgd.getCityName();
        this.streetName = userMgd.getStreetName();
        this.streetNumber = userMgd.getStreetNumber();
        this.active = userMgd.isActive();
    }


}
