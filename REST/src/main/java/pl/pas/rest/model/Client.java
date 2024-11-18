package pl.pas.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.ClientMgd;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Getter @Setter
public class Client extends AbstractEntity {

    private String firstName;
    private String lastName;
    private String email;
    private ClientType clientType;
    private String cityName;
    private String streetName;
    private String streetNumber;
    private List<Rent> currentRents = new ArrayList<>();

    public Client(UUID id, String firstName, String lastName, String email,
                  ClientType clientType, String cityName, String streetName, String streetNumber) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cityName = cityName;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.clientType = clientType;
    }


    public Client(ClientMgd clientMgd) {
        super(clientMgd.getId());
        this.firstName = clientMgd.getFirstName();
        this.lastName = clientMgd.getLastName();
        this.email = clientMgd.getEmail();
        this.cityName = clientMgd.getCityName();
        this.streetName = clientMgd.getStreetName();
        this.streetNumber = clientMgd.getStreetNumber();
    }

    public Client(ClientMgd clientMgd, ClientType clientType) {
        super(clientMgd.getId());
        this.firstName = clientMgd.getFirstName();
        this.lastName = clientMgd.getLastName();
        this.email = clientMgd.getEmail();
        this.cityName = clientMgd.getCityName();
        this.streetName = clientMgd.getStreetName();
        this.streetNumber = clientMgd.getStreetNumber();
        this.clientType = clientType;
    }


}
