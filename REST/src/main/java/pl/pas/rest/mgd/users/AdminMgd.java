package pl.pas.rest.mgd.users;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.model.users.User;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.UUID;

@EqualsAndHashCode(callSuper=true)
@SuperBuilder(toBuilder = true)
@Getter @Setter
@BsonDiscriminator(key = DatabaseConstants.BSON_DISCRIMINATOR_KEY , value = DatabaseConstants.ADMIN_DISCRIMINATOR)
public class AdminMgd extends UserMgd {

    @BsonCreator
    public AdminMgd(
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.USER_FIRST_NAME) String firstName,
            @BsonProperty(DatabaseConstants.USER_LAST_NAME) String lastName,
            @BsonProperty(DatabaseConstants.USER_EMAIL) String email,
            @BsonProperty(DatabaseConstants.USER_PASSWORD) String password,
            @BsonProperty(DatabaseConstants.USER_CITY_NAME) String cityName,
            @BsonProperty(DatabaseConstants.USER_STREET_NAME) String streetName,
            @BsonProperty(DatabaseConstants.USER_STREET_NUMBER) String streetNumber
    ) {
        super(id, firstName, lastName, email, password, cityName, streetName, streetNumber);
    }

    public AdminMgd(User user) {
        super(user);
    }

    public AdminMgd(Document document) {
        super(document);
    }
}
