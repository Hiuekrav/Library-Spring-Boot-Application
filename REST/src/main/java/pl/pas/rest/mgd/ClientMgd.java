package pl.pas.rest.mgd;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.model.Client;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.Objects;
import java.util.UUID;

@SuperBuilder(toBuilder = true)
@ToString
@Getter @Setter
public class ClientMgd extends AbstractEntityMgd {

    @BsonProperty(DatabaseConstants.CLIENT_FIRST_NAME)
    private String firstName;

    @BsonProperty(DatabaseConstants.CLIENT_LAST_NAME)
    private String lastName;

    @BsonProperty(DatabaseConstants.CLIENT_EMAIL)
    private String email;

    @BsonProperty(DatabaseConstants.CLIENT_CLIENT_TYPE_ID)
    private UUID clientType;

    // liczba aktywnych wypozyczen, aby nie mozna bylo przekroczyc limitu dla typu klienta
    @BsonProperty(DatabaseConstants.CLIENT_ACTIVE_RENTS)
    private Integer activeRents;

    @BsonProperty(DatabaseConstants.CLIENT_CITY_NAME)
    private String cityName;

    @BsonProperty(DatabaseConstants.CLIENT_STREET_NAME)
    private String streetName;

    @BsonProperty(DatabaseConstants.CLIENT_STREET_NUMBER)
    private String streetNumber;


    @BsonCreator
    public ClientMgd(
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.CLIENT_FIRST_NAME) String firstName,
            @BsonProperty(DatabaseConstants.CLIENT_LAST_NAME) String lastName,
            @BsonProperty(DatabaseConstants.CLIENT_EMAIL) String email,
            @BsonProperty(DatabaseConstants.CLIENT_CLIENT_TYPE_ID) UUID type_id,
            @BsonProperty(DatabaseConstants.CLIENT_ACTIVE_RENTS) Integer activeRents,
            @BsonProperty(DatabaseConstants.CLIENT_CITY_NAME) String cityName,
            @BsonProperty(DatabaseConstants.CLIENT_STREET_NAME) String streetName,
            @BsonProperty(DatabaseConstants.CLIENT_STREET_NUMBER) String streetNumber ) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.clientType = type_id;
        this.activeRents = activeRents;
        this.cityName = cityName;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
    }

    public ClientMgd(Client client) {
        super(client.getId());
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.clientType = client.getClientType().getId();
        this.email = client.getEmail();
        this.cityName = client.getCityName();
        this.streetName = client.getStreetName();
        this.streetNumber = client.getStreetNumber();
        this.activeRents = client.getCurrentRents().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientMgd clientMgd = (ClientMgd) o;
        return Objects.equals(super.getId(), clientMgd.getId()) && Objects.equals(firstName, clientMgd.firstName) && Objects.equals(lastName, clientMgd.lastName) && Objects.equals(email, clientMgd.email) && Objects.equals(clientType, clientMgd.clientType) && Objects.equals(activeRents, clientMgd.activeRents) && Objects.equals(cityName, clientMgd.cityName) && Objects.equals(streetName, clientMgd.streetName) && Objects.equals(streetNumber, clientMgd.streetNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, clientType, activeRents, cityName, streetName, streetNumber);
    }
}
