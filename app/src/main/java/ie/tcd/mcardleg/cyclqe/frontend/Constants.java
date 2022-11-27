package ie.tcd.mcardleg.cyclqe.frontend;

public class Constants {

    public static final String RAW_ACCELEROMETER_TABLE = "CycLQE_Raw_Accelerometer_Data_Table";
    public static final String RAW_GPS_TABLE = "CycLQE_Raw_GPS_Data_Table";
    public static final String PROCESSED_DATA_TABLE = "CycLQE_Processed_Data_Table";

    public static final String UUID = "uuid";
    public static final String USER_ID = "userId";
    public static final String TTL = "expiration";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ACCELERATION = "acceleration";
    public static final String DATE_TIME = "dateTime";

    public static final Integer EXPIRATION_TIME = 15768000;

    public static final Float ORANGE_MARKER_THRESHOLD = 1.2F;
    public static final Float RED_MARKER_THRESHOLD = 1.3F;

    public static final Double DUBLIN_NORTHWEST_LAT = 53.3494;
    public static final Double DUBLIN_NORTHWEST_LON = -6.32;
    public static final Double DUBLIN_SOUTHEAST_LAT = 53.3414;
    public static final Double DUBLIN_SOUTHEAST_LON = -6.23;
    public static final Integer MAP_ZOOM = 13;

    public static final Integer USER_ID_VALUE = 12345;

    public static final String BLEEPER_API_URL = "https://data.smartdublin.ie/bleeperbike-api/free_bike_status/";

    public static final String AWS_ACCESS_KEY = "AKIAZNS5DHNSNI6775KH";
    public static final String AWS_SECRET_ACCESS_KEY = "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";
    public static final String AWS_REGION = "eu-west-1";
    public static final String AWS_ENDPOINT = "dynamodb.eu-west-1.amazonaws.com";

    public static final String ACCEL_FILE_PREFIX = "accel";
    public static final String GPS_FILE_PREFIX = "gps";

    public static final String DATA_JSON_FIELD = "data";
    public static final String BIKE_JSON_FIELD = "bikes";
    public static final String LATITUDE_JSON_FIELD = "lat";
    public static final String  LONGITUDE_JSON_FIELD = "lon";
}
