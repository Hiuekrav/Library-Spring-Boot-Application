package pl.pas.rest.repositories.interfaces;

import pl.pas.rest.mgd.ClientTypeMgd;

import java.util.UUID;

public interface IClientTypeRepository extends IObjectRepository<ClientTypeMgd> {

    ClientTypeMgd findAnyClientType(UUID id);

}
