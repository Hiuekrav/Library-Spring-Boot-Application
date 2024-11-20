package pl.pas.rest.utils.consts;

import pl.pas.rest.mgd.*;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.mgd.BookMgd;

public class DatabaseConstants {

    public static final String connectionString = "mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_three_nodes";

    public static final String ID = "_id";

    public static final String DATABASE_NAME = "library";

    //Book
    public static final String BSON_DISCRIMINATOR_KEY = "_clazz";
    public static final String BOOK_TITLE = "title";
    public static final String BOOK_AUTHOR = "author";
    public static final String BOOK_ARCHIVE = "archive";
    public static final String BOOK_RENTED = "rented";
    public static final String BOOK_NUMBER_OF_PAGES = "numberOfPages";
    public static final String BOOK_PUBLISHED_DATE = "publishedDate";
    public static final String BOOK_GENRE = "genre";

    //User
    public static final String USER_FIRST_NAME = "firstName";
    public static final String USER_LAST_NAME = "lastName";
    public static final String USER_EMAIL = "email";
    public static final String USER_ACTIVE_RENTS = "activeRents";
    public static final String USER_PASSWORD = "password";
    public static final String USER_CITY_NAME = "cityName";
    public static final String USER_STREET_NAME = "streetName";
    public static final String USER_STREET_NUMBER = "streetNumber";

    //Rent
    public static final String RENT_BEGIN_TIME = "beginTime";
    public static final String RENT_END_TIME = "endTime";

    public static final String RENT_ACTIVE = "active";
    public static final String RENT_READER = "reader";
    public static final String RENT_BOOK = "book";
    public static final String RENT_READER_ID = "reader._id";
    public static final String RENT_BOOK_ID = "book._id";

    //Users collection
    public static final String USER_ACTIVE = "active";
    public static final String USER_DISCRIMINATOR = "user";
    public static final String READER_DISCRIMINATOR = "reader";
    public static final String LIBRARIAN_DISCRIMINATOR = "librarian";
    public static final String ADMIN_DISCRIMINATOR = "admin";

    //Collection names
    public static final String READER_COLLECTION_NAME = "readers";
    public static final String BOOK_COLLECTION_NAME = "books";
    public static final String RENT_ACTIVE_COLLECTION_NAME = "active_rents";
    public static final String RENT_ARCHIVE_COLLECTION_NAME = "archive_rents";
    public static final String USER_COLLECTION_NAME = "users";

    //Collection types
    public static final Class<UserMgd> USER_COLLECTION_TYPE = UserMgd.class;
    public static final Class<BookMgd> BOOK_COLLECTION_TYPE = BookMgd.class;
    public static final Class<RentMgd> RENT_COLLECTION_TYPE = RentMgd.class;

}
