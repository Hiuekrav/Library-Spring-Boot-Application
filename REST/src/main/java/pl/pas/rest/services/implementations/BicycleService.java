package pl.pas.rest.services.implementations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.pas.dto.create.BicycleCreateDTO;
import pl.pas.dto.update.BicycleUpdateDTO;
import pl.pas.rest.mgd.BicycleMgd;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.model.Bicycle;
import pl.pas.rest.repositories.implementations.BicycleRepository;
import pl.pas.rest.repositories.implementations.RentRepository;
import pl.pas.rest.repositories.interfaces.IBicycleRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.services.interfaces.IBicycleService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class BicycleService extends ObjectService implements IBicycleService {
    private final IBicycleRepository bicycleRepository;
    private final IRentRepository rentRepository;

    public BicycleService() {
        super();
        this.bicycleRepository = new BicycleRepository(super.getClient(), BicycleMgd.class);
        this.rentRepository = new RentRepository(super.getClient(), RentMgd.class);
    }


    @Override
    public Bicycle createBicycle(BicycleCreateDTO bicycleCreateDTO) {
        BicycleMgd bicycleMgd =  new BicycleMgd(
                UUID.randomUUID(),
                bicycleCreateDTO.getPlateNumber(),
                bicycleCreateDTO.getBasePrice(),
                false,
                0,
                bicycleCreateDTO.getPedalNumber()
        );
        return new Bicycle(bicycleRepository.save(bicycleMgd));
    }

    @Override
    public Bicycle findBicycleById(UUID id) {
        return new Bicycle(bicycleRepository.findById(id));
    }

    @Override
    public Bicycle findBicycleByPlateNumber(String plateNumber) {
        return new Bicycle(bicycleRepository.findByPlateNumber(plateNumber));
    }

    @Override
    public List<Bicycle> findAll() {
        return bicycleRepository.findAll().stream().map(Bicycle::new).toList();
    }

    @Override
    public Bicycle updateBicycle(BicycleUpdateDTO updateDTO) {
        BicycleMgd modifiedBicycle = BicycleMgd.builder().
                id(updateDTO.getId()).
                plateNumber(updateDTO.getPlateNumber()).
                basePrice(updateDTO.getBasePrice()).
                pedalsNumber(updateDTO.getPedalNumber()).
                archive(updateDTO.isArchive()).build();
        bicycleRepository.findById(updateDTO.getId());
        return new Bicycle(bicycleRepository.save(modifiedBicycle));
    }

    @Override
    public void removeBicycle(UUID vehicleId) {
        BicycleMgd bicycle = bicycleRepository.findById(vehicleId);
        if (bicycle.getRented() == 1 || !rentRepository.findAllArchivedByVehicleId(vehicleId).isEmpty()) {
            throw new RuntimeException ("Bicycle with provided ID has active or archived rents. Unable to delete Bicycle!");
        }
        bicycleRepository.deleteById(vehicleId);
    }

}
