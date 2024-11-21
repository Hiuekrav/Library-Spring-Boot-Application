package pl.pas.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.model.users.User;

import java.time.LocalDateTime;
import java.util.UUID;
@SuperBuilder(toBuilder = true)
@Getter @Setter
public class Rent extends AbstractEntity {

    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private User reader;
    private Book book;

    public Rent(UUID id, LocalDateTime beginTime, LocalDateTime endTime, User reader, Book book) {
        super(id);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.reader = reader;
        this.book = book;
    }

    public Rent(RentMgd rentMgd, User reader, Book book) {
        super(rentMgd.getId());
        this.beginTime = rentMgd.getBeginTime();
        this.endTime = rentMgd.getEndTime();
        this.reader = reader;
        this.book = book;
    }

    public Rent(RentMgd rentMgd) {
        super(rentMgd.getId());
        this.beginTime = rentMgd.getBeginTime();
        this.endTime = rentMgd.getEndTime();
    }

    public Rent(UUID id, LocalDateTime endTime, User reader, Book book) {
        super(id);
        this.beginTime = LocalDateTime.now();
        this.endTime = endTime;
        this.reader = reader;
        this.book = book;
    }
}
