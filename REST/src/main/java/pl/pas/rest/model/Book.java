package pl.pas.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.BookMgd;

import java.time.LocalDate;
import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Getter @Setter
public class Book extends AbstractEntity {

    public enum Genre {
        FANTASY,
        THRILLER,
        ROMANCE,
        HORROR
    }

    private String title;
    private String author;
    private Integer numberOfPages;
    private Genre genre;
    private LocalDate publishedDate;
    private boolean rented;
    private boolean archive;

    public Book(UUID id, String title, String author, Integer numberOfPages,
                Genre genre, LocalDate publishedDate) {
        super(id);
        this.title = title;
        this.author = author;
        this.numberOfPages = numberOfPages;
        this.genre = genre;
        this.publishedDate = publishedDate;
        this.rented = false;
        this.archive = false;
    }

    public Book(BookMgd bookMgd) {
        super(bookMgd.getId());
        this.title = bookMgd.getTitle();
        this.author = bookMgd.getAuthor();
        this.numberOfPages = bookMgd.getNumberOfPages();
        this.genre = bookMgd.getGenre();
        this.rented = bookMgd.getRented() == 1;
        this.archive = bookMgd.isArchive();
    }
}
