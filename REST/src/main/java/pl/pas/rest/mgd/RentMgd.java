package pl.pas.rest.mgd;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.model.Rent;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter
public class RentMgd extends AbstractEntityMgd{

     @BsonProperty(DatabaseConstants.RENT_BEGIN_TIME)
     private LocalDateTime beginTime;

     @BsonProperty(DatabaseConstants.RENT_END_TIME)
     private LocalDateTime endTime;

     @BsonProperty(DatabaseConstants.CLIENT)
     private ClientMgd client;

     @BsonProperty(DatabaseConstants.VEHICLE)
     private VehicleMgd vehicle;

     @BsonProperty(DatabaseConstants.RENT_RENT_COST)
     private Double rentCost;

    @BsonCreator
    public RentMgd(
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.RENT_BEGIN_TIME) LocalDateTime beginTime,
            @BsonProperty(DatabaseConstants.RENT_END_TIME) LocalDateTime endTime,
            @BsonProperty(DatabaseConstants.CLIENT) ClientMgd client,
            @BsonProperty(DatabaseConstants.VEHICLE) VehicleMgd vehicle) {
        super(id);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.client = client;
        this.vehicle = vehicle;
        this.rentCost = ChronoUnit.HOURS.between(endTime, beginTime) * vehicle.getBasePrice();
    }


    public RentMgd(Rent rent) {
        super(rent.getId());
        this.beginTime = rent.getBeginTime();
        this.endTime = rent.getEndTime();
        this.rentCost = rent.getRentCost();
    }

    public RentMgd(Rent rent, ClientMgd client, VehicleMgd vehicle) {
        super(rent.getId());
        this.beginTime = rent.getBeginTime();
        this.endTime = rent.getEndTime();
        this.rentCost = rent.getRentCost();
        this.client = client;
        this.vehicle = vehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentMgd rentMgd = (RentMgd) o;
        return Objects.equals(beginTime, rentMgd.beginTime) && Objects.equals(endTime, rentMgd.endTime) && Objects.equals(client, rentMgd.client) && Objects.equals(vehicle, rentMgd.vehicle) && Objects.equals(rentCost, rentMgd.rentCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginTime, endTime, client, vehicle, rentCost);
    }
}
