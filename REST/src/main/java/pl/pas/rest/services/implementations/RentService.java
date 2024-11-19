package pl.pas.rest.services.implementations;

import com.mongodb.MongoWriteException;
import com.mongodb.client.ClientSession;
import lombok.Getter;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.rest.mgd.users.ClientMgd;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.mgd.CarMgd;
import pl.pas.rest.model.users.User;

import pl.pas.rest.model.Rent;
import pl.pas.rest.model.Car;
import pl.pas.rest.repositories.implementations.CarRepository;
import pl.pas.rest.repositories.implementations.RentRepository;
import pl.pas.rest.repositories.implementations.UserRepository;
import pl.pas.rest.repositories.interfaces.ICarRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.repositories.interfaces.IVehicleRepository;
import pl.pas.rest.services.interfaces.IRentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class RentService extends ObjectService implements IRentService {

    private final IClientRepository clientRepository;
    private final IRentRepository rentRepository;
    private final IVehicleRepository<VehicleMgd> vehicleRepository;
    private final IClientTypeRepository clientTypeRepository;


    public RentService() {
        super();
        this.rentRepository = new RentRepository(super.getClient(), RentMgd.class);
        this.vehicleRepository = new VehicleRepository<>(super.getClient(), VehicleMgd.class);
        this.clientRepository = new ClientRepository(super.getClient(), ClientMgd.class);
        this.clientTypeRepository = new ClientTypeRepository(super.getClient(), ClientTypeMgd.class);
    }


    @Override
    public Rent createRent(RentCreateDTO createRentDTO) {
        ClientSession clientSession  = super.getClient().startSession();
        try {
            clientSession.startTransaction();
            UserMgd foundClient = clientRepository.findById(createRentDTO.clientId());
            CarMgd foundCar = carRepository.findById(createRentDTO.carId());

            if (foundClient == null && foundCar == null) {
                throw new RuntimeException("RentRepository: User or Vehicle not found");
            }

            if (createRentDTO.endTime().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("RentRepository: Invalid end time ");

            }

            UserMgd userMgd = clientRepository.findById(createRentDTO.clientId());


            foundVehicle = vehicleRepository.changeRentedStatus(foundVehicle.getId(), true);

            Rent rent = new Rent(
                    UUID.randomUUID(),
                    createRentDTO.endTime(),
                    new User(foundClient),
                    new Car(foundCar)
            );
            RentMgd rentMgd = new RentMgd(rent, foundClient, foundVehicle);
            rentRepository.save(rentMgd);
            return rent;
        }
        catch (MongoWriteException e) {
            clientSession.abortTransaction();
            clientSession.close();
            throw new RuntimeException("RentRepository: Vehicle already rented!");
        }
        catch (RuntimeException e) {
            clientSession.abortTransaction();
            clientSession.close();
            throw e;
        }
    }

    @Override
    public Rent findRentById(UUID id) {
        RentMgd rentMgd = rentRepository.findById(id);
        CarMgd carMgd = carRepository.findById(rentMgd.getCarMgd().getId());
        UserMgd userMgd = clientRepository.findById(rentMgd.getClient().getId());

        return new Rent(rentMgd, new User(userMgd), new Car(carMgd));

    }

    @Override
    public List<Rent> findAllActiveByClientID(UUID clientId) {
        return rentRepository.findAllActiveByClientId(clientId).stream().map(Rent::new).toList();
    }

    @Override
    public List<Rent> findAllArchivedByClientID(UUID clientId) {
        return rentRepository.findAllArchivedByClientId(clientId).stream().map(Rent::new).toList();
    }

    @Override
    public List<Rent> findAllActiveByVehicleID(UUID vehicleId) {
        return rentRepository.findAllActiveByVehicleId(vehicleId).stream().map(Rent::new).toList();
    }

    @Override
    public List<Rent> findAllArchivedByVehicleID(UUID vehicleId) {
        return rentRepository.findAllArchivedByVehicleId(vehicleId).stream().map(Rent::new).toList();
    }

    @Override
    public Rent updateRent(UUID id, LocalDateTime endTime) {

        RentMgd rentMgd = rentRepository.findActiveById(id);
        CarMgd carMgd = carRepository.findById(rentMgd.getCarMgd().getId());
        UserMgd userMgd = clientRepository.findById(rentMgd.getClient().getId());

        Rent rent = findRentById(id);

        try {
            if (!endTime.isAfter(rentMgd.getEndTime()) ) {
                throw new RuntimeException("RentRepository: New Rent end time cannot be before current rent end time");
            }
            rent.setEndTime(endTime);
            rent.recalculateRentCost();
            rentRepository.save(new RentMgd(rent, userMgd, carMgd));
            return rent;

        } catch (RuntimeException e) {
            throw new RuntimeException("RentRepository: New Rent end time cannot be before current rent end time");
        }

    }

    @Override
    public void endRent(UUID id) {
        ClientSession clientSession = rentRepository.getClient().startSession();
        try {
            clientSession.startTransaction();
            RentMgd rent = rentRepository.findActiveById(id);
            vehicleRepository.changeRentedStatus(rent.getVehicle().getId(), false);
            clientRepository.increaseActiveRents(rent.getClient().getId(), -1);
            rentRepository.moveRentToArchived(id);
            clientSession.commitTransaction();
        } catch (RuntimeException e) {
            clientSession.abortTransaction();
            clientSession.close();
            throw e;
        }
    }


}
