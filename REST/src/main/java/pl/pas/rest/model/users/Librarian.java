package pl.pas.rest.model.users;

import lombok.Getter;
import lombok.Setter;
import pl.pas.rest.mgd.users.LibrarianMgd;
import pl.pas.rest.mgd.users.UserMgd;

import java.util.UUID;

@Getter @Setter
public class Librarian extends User {

    public Librarian(UUID id, String firstName, String lastName, String email, String password,
                     String cityName, String streetName, String streetNumber) {
        super(id, firstName, lastName, email, password, cityName, streetName, streetNumber);
    }

    public Librarian(LibrarianMgd librarianMgd) {
        super(librarianMgd);
    }
}
