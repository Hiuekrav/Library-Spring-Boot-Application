package pl.pas.rest.repositories.interfaces;

import pl.pas.rest.mgd.ClientMgd;

import java.util.List;
import java.util.UUID;


public interface IClientRepository extends IObjectRepository<ClientMgd> {
    ClientMgd findByEmail(String email);

    ClientMgd increaseActiveRents(UUID id, Integer number);

    List<ClientMgd> findByType(Class<?> type);
}
