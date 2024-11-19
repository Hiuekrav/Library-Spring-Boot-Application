package pl.pas.rest.utils.mappers;

import pl.pas.dto.output.CarOutputDTO;
import pl.pas.rest.model.Car;

public class CarMapper {

    public static CarOutputDTO carToCarOutputDTO(Car car) {
        return new CarOutputDTO(
                car.getId(),
                car.getPlateNumber(),
                car.getBasePrice(),
                car.getEngineDisplacement(),
                car.getTransmissionType().toString()
        );
    }
}
