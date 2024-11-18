package pl.pas.rest.services.implementations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.pas.dto.create.MopedCreateDTO;
import pl.pas.dto.update.MopedUpdateDTO;
import pl.pas.rest.mgd.MopedMgd;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.model.Moped;
import pl.pas.rest.repositories.implementations.MopedRepository;
import pl.pas.rest.repositories.implementations.RentRepository;
import pl.pas.rest.repositories.interfaces.IMopedRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.services.interfaces.IMopedService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class MopedService extends ObjectService implements IMopedService {

    private final IMopedRepository mopedRepository;
    private final IRentRepository rentRepository;

    public MopedService() {
        super();
        this.mopedRepository = new MopedRepository(super.getClient(), MopedMgd.class);
        this.rentRepository = new RentRepository(super.getClient(), RentMgd.class);
    }

    @Override
    public Moped createMoped(MopedCreateDTO mopedCreateDTO) {
        MopedMgd moped =  new MopedMgd(
                UUID.randomUUID(),
                mopedCreateDTO.getPlateNumber(),
                mopedCreateDTO.getBasePrice(),
                false,
                0,
                mopedCreateDTO.getEngineDisplacement()
        );
        return new Moped(mopedRepository.save(moped));
    }

    @Override
    public Moped findMopedById(UUID id) {
        return new Moped(mopedRepository.findById(id));
    }

    @Override
    public Moped findMopedByPlateNumber(String plateNumber) {
        return new Moped(mopedRepository.findByPlateNumber(plateNumber));
    }

    @Override
    public List<Moped> findAll() {
        return mopedRepository.findAll().stream().map(Moped::new).toList();
    }

    @Override
    public Moped updateMoped(MopedUpdateDTO updateDTO) {
        MopedMgd modifiedMoped = MopedMgd.builder().
                id(updateDTO.getId()).
                plateNumber(updateDTO.getPlateNumber()).
                basePrice(updateDTO.getBasePrice()).
                archive(updateDTO.isArchive()).
                engineDisplacement(updateDTO.getEngineDisplacement())
                .build();
        return new Moped(mopedRepository.save(modifiedMoped));
    }

    @Override
    public void removeMoped(UUID vehicleId) {
        MopedMgd mopedMgd = mopedRepository.findById(vehicleId);
        if (mopedMgd.getRented() == 1 || !rentRepository.findAllArchivedByVehicleId(vehicleId).isEmpty()) {
            throw new RuntimeException ("Moped with provided ID has active or archived rents. Unable to delete Moped!");
        }
        mopedRepository.deleteById(vehicleId);
    }
}
