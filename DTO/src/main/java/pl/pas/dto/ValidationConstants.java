package pl.pas.dto;

public class ValidationConstants {

    public static final int FIRST_NAME_MIN_LENGTH = 2;
    public static final int FIRST_NAME_MAX_LENGTH = 50;

    public static final String FIRST_NAME_TOO_SHORT = "first.name.too.short";
    public static final String FIRST_NAME_TOO_LONG = "first.name.too.short";


    public static final int LAST_NAME_MIN_LENGTH = 2;
    public static final int LAST_NAME_MAX_LENGTH = 100;

    public static final String LAST_NAME_TOO_SHORT = "last.name.too.short";
    public static final String LAST_NAME_TOO_LONG = "last.name.too.short";

    public static final int EMAIL_MIN_LENGTH = 8;
    public static final int EMAIL_MAX_LENGTH = 64;

    public static final String EMAIL_INVALID_FORMAT = "email.invalid.format";
    public static final String EMAIL_TOO_SHORT = "email.too.short";
    public static final String EMAIL_TOO_LONG = "email.too.long";

    public static final String EMAIL_BLANK = "email.blank";

}
