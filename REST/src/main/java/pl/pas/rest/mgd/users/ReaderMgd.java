package pl.pas.rest.mgd.users;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import pl.pas.rest.model.users.User;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.UUID;

@EqualsAndHashCode(callSuper=true)
@SuperBuilder(toBuilder = true)
@Getter @Setter
@BsonDiscriminator(key = DatabaseConstants.BSON_DISCRIMINATOR_KEY , value = DatabaseConstants.READER_DISCRIMINATOR)
public class ReaderMgd extends UserMgd{

    public ReaderMgd(UUID id, String firstName, String lastName, String email, String password,
                     String cityName, String streetName, String streetNumber) {
        super(id, firstName, lastName, email, password, cityName, streetName, streetNumber);
    }

    public ReaderMgd(User user) {
        super(user);
    }

    public ReaderMgd(Document document) {
        super(document);
    }
}
