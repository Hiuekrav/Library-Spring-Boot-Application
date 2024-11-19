package pl.pas.rest.mgd;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.model.Rent;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Getter @Setter
public class RentMgd extends AbstractEntityMgd{

     @BsonProperty(DatabaseConstants.RENT_BEGIN_TIME)
     private LocalDateTime beginTime;

     @BsonProperty(DatabaseConstants.RENT_END_TIME)
     private LocalDateTime endTime;

     @BsonProperty(DatabaseConstants.RENT_CLIENT)
     private UserMgd client;

     @BsonProperty(DatabaseConstants.RENT_CAR)
     private CarMgd carMgd;

     @BsonProperty(DatabaseConstants.RENT_RENT_COST)
     private Double rentCost;

    @BsonCreator
    public RentMgd(
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.RENT_BEGIN_TIME) LocalDateTime beginTime,
            @BsonProperty(DatabaseConstants.RENT_END_TIME) LocalDateTime endTime,
            @BsonProperty(DatabaseConstants.RENT_CLIENT) UserMgd client,
            @BsonProperty(DatabaseConstants.RENT_CAR) CarMgd carMgd) {
        super(id);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.client = client;
        this.carMgd = carMgd;
        this.rentCost = ChronoUnit.HOURS.between(endTime, beginTime) * carMgd.getBasePrice();
    }

    public RentMgd(Rent rent, UserMgd client, CarMgd carMgd) {
        super(rent.getId());
        this.beginTime = rent.getBeginTime();
        this.endTime = rent.getEndTime();
        this.rentCost = rent.getRentCost();
        this.client = client;
        this.carMgd = carMgd;
    }
}
