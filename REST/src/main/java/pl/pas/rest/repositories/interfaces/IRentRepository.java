package pl.pas.rest.repositories.interfaces;

import pl.pas.rest.mgd.RentMgd;

import java.util.List;
import java.util.UUID;

public interface IRentRepository extends IObjectRepository<RentMgd> {

    RentMgd findActiveById(UUID id);
    RentMgd findArchiveById(UUID id);

    // By client
    List<RentMgd> findAllActiveByReaderId(UUID readerId);
    List<RentMgd> findAllByReaderId(UUID readerId);
    List<RentMgd> findAllArchivedByReaderId(UUID readerId);

    // By vehicle
    List<RentMgd> findAllActiveByBookId(UUID bookId);
    List<RentMgd> findAllByBookId(UUID bookId);
    List<RentMgd> findAllArchivedByBookId(UUID bookId);

    void moveRentToArchived(UUID rentId);
}
