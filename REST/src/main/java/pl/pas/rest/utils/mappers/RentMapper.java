package pl.pas.rest.utils.mappers;

import pl.pas.dto.output.RentOutputDTO;
import pl.pas.rest.model.Rent;

public class RentMapper {
    public static RentOutputDTO toRentOutputDTO(Rent rent) {
        return new RentOutputDTO(
                rent.getId(),
                UserMapper.toUserDataOutputDTO(rent.getReader()),
                BookMapper.toBookDataOutputDTO(rent.getBook()),
                rent.getBeginTime(),
                rent.getEndTime(),
                rent.isActive()
        );
    }
}
