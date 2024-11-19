package pl.pas.rest.repositories.interfaces;

import pl.pas.rest.mgd.users.UserMgd;

import java.util.UUID;

public interface IUserRepository<T extends UserMgd> extends IObjectRepository<T> {

    T findById(UUID id);

    UserMgd findAnyUserById(UUID id);

    T findByEmail(String email);
}
