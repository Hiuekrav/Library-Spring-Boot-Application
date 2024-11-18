package pl.pas.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.RentMgd;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
@SuperBuilder(toBuilder = true)
@Getter @Setter
public class Rent extends AbstractEntity {

    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Client client;
    private Vehicle vehicle;
    private Double rentCost;
    private boolean active;

    public Rent(UUID id, LocalDateTime beginTime, LocalDateTime endTime, Client client, Vehicle vehicle, boolean active) {
        super(id);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.client = client;
        this.vehicle = vehicle;
        this.active = active;
        this.rentCost = ChronoUnit.HOURS.between(beginTime, endTime.plusHours(1)) * vehicle.getBasePrice() - client.getClientType().getDiscount();
    }

    public void recalculateRentCost() {
        this.rentCost = ChronoUnit.HOURS.between(beginTime, endTime.plusHours(1)) * vehicle.getBasePrice() - client.getClientType().getDiscount();
    }

    public Rent(RentMgd rentMgd, Client client, Vehicle vehicle) {
        super(rentMgd.getId());
        this.beginTime = rentMgd.getBeginTime();
        this.endTime = rentMgd.getEndTime();
        this.rentCost = rentMgd.getRentCost();
        this.client = client;
        this.vehicle = vehicle;
    }

    public Rent(RentMgd rentMgd) {
        super(rentMgd.getId());
        this.beginTime = rentMgd.getBeginTime();
        this.endTime = rentMgd.getEndTime();
        this.rentCost = rentMgd.getRentCost();
    }

    public Rent(UUID id, LocalDateTime endTime, Client client, Vehicle vehicle) {
        super(id);
        this.beginTime = LocalDateTime.now();
        this.endTime = endTime;
        this.client = client;
        this.vehicle = vehicle;
        this.active = true;
        this.rentCost = ChronoUnit.HOURS.between(beginTime, endTime.plusHours(1)) * vehicle.getBasePrice() - client.getClientType().getDiscount();
    }

}
