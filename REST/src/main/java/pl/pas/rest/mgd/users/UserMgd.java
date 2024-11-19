package pl.pas.rest.mgd.users;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.mgd.AbstractEntityMgd;
import pl.pas.rest.model.users.User;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.Objects;
import java.util.UUID;

@SuperBuilder(toBuilder = true)
@ToString
@Getter @Setter
@BsonDiscriminator(key = DatabaseConstants.BSON_DISCRIMINATOR_KEY , value = DatabaseConstants.USER_DISCRIMINATOR)
public class UserMgd extends AbstractEntityMgd {

    @BsonProperty(DatabaseConstants.USER_FIRST_NAME)
    private String firstName;

    @BsonProperty(DatabaseConstants.USER_LAST_NAME)
    private String lastName;

    @BsonProperty(DatabaseConstants.USER_EMAIL)
    private String email;

    @BsonProperty(DatabaseConstants.USER_PASSWORD)
    private String password;

    @BsonProperty(DatabaseConstants.CLIENT_CITY_NAME)
    private String cityName;

    @BsonProperty(DatabaseConstants.CLIENT_STREET_NAME)
    private String streetName;

    @BsonProperty(DatabaseConstants.CLIENT_STREET_NUMBER)
    private String streetNumber;

    @BsonProperty(DatabaseConstants.USER_ACTIVE)
    private boolean active;

    @BsonCreator
    public UserMgd(
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.USER_FIRST_NAME) String firstName,
            @BsonProperty(DatabaseConstants.USER_LAST_NAME) String lastName,
            @BsonProperty(DatabaseConstants.USER_EMAIL) String email,
            @BsonProperty(DatabaseConstants.USER_PASSWORD) String password,
            @BsonProperty(DatabaseConstants.CLIENT_CITY_NAME) String cityName,
            @BsonProperty(DatabaseConstants.CLIENT_STREET_NAME) String streetName,
            @BsonProperty(DatabaseConstants.CLIENT_STREET_NUMBER) String streetNumber) {
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

    public UserMgd(User user) {
        super(user.getId());
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.cityName = user.getCityName();
        this.streetName = user.getStreetName();
        this.streetNumber = user.getStreetNumber();
        this.active = user.isActive();
    }

    public UserMgd(Document document) {
        super(
                document.get(DatabaseConstants.ID, UUID.class)
        );
        this.firstName = document.getString(DatabaseConstants.USER_FIRST_NAME);
        this.lastName = document.getString(DatabaseConstants.USER_LAST_NAME);
        this.email = document.getString(DatabaseConstants.USER_EMAIL);
        this.password = document.getString(DatabaseConstants.USER_PASSWORD);
        this.cityName = document.getString(DatabaseConstants.CLIENT_CITY_NAME);
        this.streetName = document.getString(DatabaseConstants.CLIENT_STREET_NAME);
        this.streetNumber = document.getString(DatabaseConstants.CLIENT_STREET_NUMBER);
        this.active = document.getBoolean(DatabaseConstants.USER_ACTIVE);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMgd userMgd = (UserMgd) o;
        return Objects.equals(super.getId(), userMgd.getId())
                && Objects.equals(firstName, userMgd.firstName)
                && Objects.equals(lastName, userMgd.lastName)
                && Objects.equals(email, userMgd.email)
                && Objects.equals(password, userMgd.password)
                && Objects.equals(cityName, userMgd.cityName)
                && Objects.equals(streetName, userMgd.streetName)
                && Objects.equals(streetNumber, userMgd.streetNumber)
                && Objects.equals(active, userMgd.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password, cityName, streetName, streetNumber, active);
    }
}
