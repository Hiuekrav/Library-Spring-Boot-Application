package pl.pas.rest.mgd;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.model.Book;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.time.LocalDate;
import java.util.UUID;

@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Getter @Setter
@ToString

public class BookMgd extends AbstractEntityMgd {

    @BsonProperty(DatabaseConstants.BOOK_TITLE)
    private String title;

    @BsonProperty(DatabaseConstants.BOOK_AUTHOR)
    private String author;

    @BsonProperty(DatabaseConstants.BOOK_NUMBER_OF_PAGES)
    private Integer numberOfPages;

    @BsonProperty(DatabaseConstants.BOOK_GENRE)
    private Book.Genre genre;

    @BsonProperty(DatabaseConstants.BOOK_PUBLISHED_DATE)
    private LocalDate publishedDate;

    @BsonProperty(DatabaseConstants.BOOK_RENTED)
    private Integer rented;

    @BsonProperty(DatabaseConstants.BOOK_ARCHIVE)
    private boolean archive;

    @BsonCreator
    public BookMgd(
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.BOOK_TITLE) String title,
            @BsonProperty(DatabaseConstants.BOOK_AUTHOR) String author,
            @BsonProperty(DatabaseConstants.BOOK_NUMBER_OF_PAGES) Integer numberOfPages,
            @BsonProperty(DatabaseConstants.BOOK_GENRE) Book.Genre genre,
            @BsonProperty(DatabaseConstants.BOOK_PUBLISHED_DATE) LocalDate publishedDate,
            @BsonProperty(DatabaseConstants.BOOK_RENTED) Integer rented,
            @BsonProperty(DatabaseConstants.BOOK_ARCHIVE) boolean archive)
             {
        super(id);
        this.title = title;
        this.author = author;
        this.numberOfPages = numberOfPages;
        this.genre = genre;
        this.publishedDate = publishedDate;
        this.rented = rented;
        this.archive = archive;

    }

    public BookMgd(Book book) {
        super(book.getId());
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.numberOfPages = book.getNumberOfPages();
        this.genre = book.getGenre();
        this.publishedDate = book.getPublishedDate();
        this.rented = book.isRented() ? 1 : 0;
        this.archive = book.isArchive();
    }

    public BookMgd(Document document) {
        super(document.get(DatabaseConstants.ID, UUID.class));
        this.title = document.getString(DatabaseConstants.BOOK_TITLE);
        this.author = document.getString(DatabaseConstants.BOOK_AUTHOR);
        this.numberOfPages = document.getInteger(DatabaseConstants.BOOK_NUMBER_OF_PAGES);
        this.genre = Book.Genre.valueOf(document.getString(DatabaseConstants.BOOK_GENRE));
        this.publishedDate = LocalDate.parse(document.getString(DatabaseConstants.BOOK_PUBLISHED_DATE));
        this.rented = document.getInteger(DatabaseConstants.BOOK_RENTED);
        this.archive = document.getBoolean(DatabaseConstants.BOOK_ARCHIVE);
    }

}
