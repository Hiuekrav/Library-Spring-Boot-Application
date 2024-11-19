package pl.pas.rest.utils.consts;

import pl.pas.rest.mgd.*;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.mgd.CarMgd;

public class DatabaseConstants {

    //connection
    public static final String connectionString = "mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_three_nodes";

    // abstractEntity
    public static final String ID = "_id";

    public static final String DATABASE_NAME = "rentacar";

    //ClientEmbeddedMgd
    public static final String RENT_CLIENT = "client";

    // Vehicle
    public static final String BSON_DISCRIMINATOR_KEY = "_clazz";
    public static final String VEHICLE_DISCRIMINATOR = "vehicle";
    public static final String CAR_PLATE_NUMBER = "plateNumber";
    public static final String CAR_BASE_PRICE = "basePrice";
    public static final String CAR_ARCHIVE = "archive";
    public static final String CAR_RENTED = "rented";

    // MotorVehicle
    public static final String CAR_ENGINE_DISPLACEMENT = "engineDisplacement";

    // Moped
    public static final String MOPED_DISCRIMINATOR = "moped";

    //Car
    public static final String CAR_DISCRIMINATOR = "car";

    public static final String CAR_TRANSMISSION_TYPE = "transmissionType";

    // Bicycle
    public static final String BICYCLE_DISCRIMINATOR = "bicycle";

    public static final String BICYCLE_PEDAL_NUMBER = "pedal_number";



    // ClientType

    public static final String CLIENT_TYPE_DISCRIMINATOR = "client_type";

    public static final String DEFAULT_DISCRIMINATOR = "default";
    public static final String SILVER_DISCRIMINATOR = "silver";
    public static final String GOLD_DISCRIMINATOR = "gold";


    public static final String CLIENT_TYPE_DISCOUNT = "discount";
    public static final String CLIENT_TYPE_MAX_VEHICLES = "max_vehicles";


    // User

    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_EMAIL = "email";
    public static final String USER_ACTIVE_RENTS = "active_rents";
    public static final String USER_PASSWORD = "password";

    public static final String CLIENT_CITY_NAME = "city_name";
    public static final String CLIENT_STREET_NAME = "street_name";
    public static final String CLIENT_STREET_NUMBER = "street_number";

    public static final String CLIENT_CLIENT_TYPE_ID = "client_type_id";


    // Rent

    public static final String RENT_BEGIN_TIME = "begin_time";
    public static final String RENT_END_TIME = "end_time";

    public static final String RENT_RENT_COST = "rent_cost";
    public static final String RENT_ACTIVE = "active";
    public static final String RENT_CAR = "car";

    public static final String RENT_CLIENT_ID = "client._id";
    public static final String RENT_VEHICLE_ID = "vehicle._id";

    // Users

    public static final String USER_ACTIVE = "active";

    public static final String USER_DISCRIMINATOR = "user";
    public static final String CLIENT_DISCRIMINATOR = "client";
    public static final String MECHANIC_DISCRIMINATOR = "mechanic";
    public static final String ADMIN_DISCRIMINATOR = "admin";

    //Collection names
    public static final String CLIENT_COLLECTION_NAME = "clients";
    public static final String CAR_COLLECTION_NAME = "cars";
    public static final String RENT_ACTIVE_COLLECTION_NAME = "active_rents";
    public static final String RENT_ARCHIVE_COLLECTION_NAME = "archive_rents";
    public static final String CLIENT_TYPE_COLLECTION_NAME = "client_types";
    public static final String USER_COLLECTION_NAME = "users";

    //Collection types
    public static final Class<UserMgd> CLIENT_COLLECTION_TYPE = UserMgd.class;
    public static final Class<CarMgd> CAR_COLLECTION_TYPE = CarMgd.class;
    public static final Class<RentMgd> RENT_COLLECTION_TYPE = RentMgd.class;
    public static final Class<UserMgd> USER_TYPE_COLLECTION_TYPE = UserMgd.class;

}
