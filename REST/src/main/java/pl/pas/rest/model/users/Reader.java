package pl.pas.rest.model.users;

import lombok.Getter;
import lombok.Setter;
import pl.pas.rest.mgd.users.ReaderMgd;

import java.util.UUID;

@Getter @Setter
public class Reader extends User {

    public Reader(UUID id, String firstName, String lastName, String email, String password,
                  String cityName, String streetName, String streetNumber) {
        super(id, firstName, lastName, email, password, cityName, streetName, streetNumber);
    }

    public Reader(ReaderMgd readerMgd) {
        super(readerMgd);
    }
}
