package ie.tcd.mcardleg.cyclqe.frontend.collect;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ie.tcd.mcardleg.cyclqe.frontend.Constants;
import ie.tcd.mcardleg.cyclqe.frontend.R;

public class CSVHandler {

    private Context context;
    private List<Document> accelerometerData;
    private List<Document> GPSData;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public CSVHandler(Context context) {
        this.context = context;
        try {
            readAllRawFiles();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public List<Document> getAccelerometerData() {
        return accelerometerData;
    }

    public List<Document> getGPSData() {
        return GPSData;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void readAllRawFiles() {
        accelerometerData = new ArrayList<Document>();
        GPSData = new ArrayList<Document>();

        try {
            Field[] fields = R.raw.class.getDeclaredFields();

            for(int count=0; count < fields.length; count++) {
                String fileName = fields[count].getName();

                if(fileName.substring(0,5).equals(Constants.ACCEL_FILE_PREFIX)) {
                    accelerometerData = parseData(
                            fields[count].getInt(fields[count]), accelerometerData, true);
                }
                else if(fileName.substring(0,3).equals(Constants.GPS_FILE_PREFIX)) {
                    GPSData = parseData(
                            fields[count].getInt(fields[count]), GPSData, false);
                }
            }

        } catch(Exception e) {
            System.err.println(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Document> parseData(int resourceID, List<Document> dataPoints, Boolean isAcceleration) {

        try {
            InputStream is = context.getResources().openRawResource(resourceID);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8"))
            );

            String line = reader.readLine(); //reading column titles
            if (isAcceleration) {
                line = reader.readLine();
                String[] firstValues = line.split(",");
                String currTime;
                String prevTime = firstValues[0];
                Float currAccel = Float.parseFloat(firstValues[4]);
                Float maxAccel = currAccel;

                while ((line = reader.readLine()) != null) {

                    String[] values = line.split(",");
                    currTime = values[0];
                    if (currTime.equals(prevTime)) {
                        if (currAccel > maxAccel) {
                            maxAccel = currAccel;
                        }
                    }
                    else {
                        Document dp = accelerometerDocumentBuilder(prevTime, maxAccel);
                        dataPoints.add(dp);
                        maxAccel = currAccel;
                        currAccel = Float.parseFloat(values[4]);
                        prevTime = currTime;
                    }
                }
                Document dp = accelerometerDocumentBuilder(prevTime, maxAccel);
                dataPoints.add(dp);
            }
            else {
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");
                    Document dp = gpsDocumentBuilder(values[1], values[2], values[6]);
                    dataPoints.add(dp);
                }
            }

        } catch(Exception e) {
            System.err.println(e);
        }

        return dataPoints;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Document accelerometerDocumentBuilder(String dateTime, Float acceleration) {

        Document document = new Document();
        document.put(Constants.UUID, UUID.randomUUID().toString());
        document.put(Constants.USER_ID, Constants.USER_ID_VALUE);
        document.put(Constants.DATE_TIME, dateTime);
        document.put(Constants.ACCELERATION, acceleration);
        document.put(Constants.TTL, (System.currentTimeMillis() + Constants.EXPIRATION_TIME) / 1000L);

        return document;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Document gpsDocumentBuilder(
            String latitude, String longitude, String dateTime) {

        Document document = new Document();
        document.put(Constants.UUID, UUID.randomUUID().toString());
        document.put(Constants.USER_ID, Constants.USER_ID_VALUE);
        document.put(Constants.LATITUDE, Float.parseFloat(latitude));
        document.put(Constants.LONGITUDE, Float.parseFloat(longitude));
        document.put(Constants.DATE_TIME, dateTime);
        document.put(Constants.TTL, (System.currentTimeMillis() + Constants.EXPIRATION_TIME) / 1000L);

        return document;
    }
}
