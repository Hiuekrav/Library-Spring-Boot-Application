package pl.pas.dto.output;

import java.time.LocalDateTime;
import java.util.UUID;

public record RentOutputDTO(
        UUID id,
        UserDataOutputDTO userOutputDTO,
        BookDataOutputDTO bookOutputDTO,
        LocalDateTime beginTime,
        LocalDateTime endTime
){}
