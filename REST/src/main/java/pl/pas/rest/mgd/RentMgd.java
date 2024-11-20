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
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Getter @Setter
public class RentMgd extends AbstractEntityMgd{

     @BsonProperty(DatabaseConstants.RENT_BEGIN_TIME)
     private LocalDateTime beginTime;

     @BsonProperty(DatabaseConstants.RENT_END_TIME)
     private LocalDateTime endTime;

     @BsonProperty(DatabaseConstants.RENT_READER)
     private UserMgd reader;

     @BsonProperty(DatabaseConstants.RENT_BOOK)
     private BookMgd bookMgd;

    @BsonCreator
    public RentMgd(
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.RENT_BEGIN_TIME) LocalDateTime beginTime,
            @BsonProperty(DatabaseConstants.RENT_END_TIME) LocalDateTime endTime,
            @BsonProperty(DatabaseConstants.RENT_READER) UserMgd reader,
            @BsonProperty(DatabaseConstants.RENT_BOOK) BookMgd bookMgd) {
        super(id);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.reader = reader;
        this.bookMgd = bookMgd;
    }

    public RentMgd(Rent rent, UserMgd reader, BookMgd bookMgd) {
        super(rent.getId());
        this.beginTime = rent.getBeginTime();
        this.endTime = rent.getEndTime();
        this.reader = reader;
        this.bookMgd = bookMgd;
    }
}
