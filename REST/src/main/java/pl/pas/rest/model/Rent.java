package pl.pas.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.model.users.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
@SuperBuilder(toBuilder = true)
@Getter @Setter
public class Rent extends AbstractEntity {

    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private User client;
    private Car car;
    private Double rentCost;
    private boolean active;

    public Rent(UUID id, LocalDateTime beginTime, LocalDateTime endTime, User client, Car car) {
        super(id);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.client = client;
        this.car = car;
        this.active = !beginTime.isAfter(LocalDateTime.now());
        this.rentCost = ChronoUnit.HOURS.between(beginTime, endTime.plusHours(1)) * car.getBasePrice();
    }

    public void recalculateRentCost() {
        this.rentCost = ChronoUnit.HOURS.between(beginTime, endTime.plusHours(1)) * car.getBasePrice();
    }

    public Rent(RentMgd rentMgd, User client, Car car) {
        super(rentMgd.getId());
        this.beginTime = rentMgd.getBeginTime();
        this.endTime = rentMgd.getEndTime();
        this.rentCost = rentMgd.getRentCost();
        this.client = client;
        this.car = car;
    }

    public Rent(RentMgd rentMgd) {
        super(rentMgd.getId());
        this.beginTime = rentMgd.getBeginTime();
        this.endTime = rentMgd.getEndTime();
        this.rentCost = rentMgd.getRentCost();
    }

    public Rent(UUID id, LocalDateTime endTime, User client, Car car) {
        super(id);
        this.beginTime = LocalDateTime.now();
        this.endTime = endTime;
        this.client = client;
        this.car = car;
        this.active = true;
        this.rentCost = ChronoUnit.HOURS.between(beginTime, endTime.plusHours(1)) * car.getBasePrice();
    }

}
