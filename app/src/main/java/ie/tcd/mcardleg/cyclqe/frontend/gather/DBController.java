package ie.tcd.mcardleg.cyclqe.frontend.gather;

import android.os.StrictMode;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.dynamodbv2.document.ScanOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;

import java.util.ArrayList;
import java.util.List;

import ie.tcd.mcardleg.cyclqe.frontend.Constants;
import ie.tcd.mcardleg.cyclqe.frontend.Globals;
import ie.tcd.mcardleg.cyclqe.frontend.collect.CSVHandler;

public class DBController {

    private AmazonDynamoDBClient dbClient;

    public DBController() {

        // Android defaults to banning internet connection on main thread.
        // Overriding this policy as this is a POC application.
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        BasicAWSCredentials credentialsProvider = new BasicAWSCredentials(
                Constants.AWS_ACCESS_KEY,
                Constants.AWS_SECRET_ACCESS_KEY);
        dbClient = new AmazonDynamoDBClient(credentialsProvider);
        dbClient.setRegion(Region.getRegion(Constants.AWS_REGION));
        dbClient.setEndpoint(Constants.AWS_ENDPOINT);

    }

    private Boolean checkTableExists(String tableName) {
        ListTablesResult result = dbClient.listTables();
        List<String> table_names = result.getTableNames();
        if (table_names.contains(tableName)) {
            return true;
        }
        return false;
    }

    private void putDatasetInDB(List<Document> dataset, String tableName) {
        Table table = Table.loadTable(dbClient, tableName);

        if (checkTableExists(tableName)) {
            for (Document document : dataset) {
                table.putItem(document);
            }
        }
        else {
            System.out.println("Accelerometer table not found.");
        }
    }

    public void putDatasetsInDB(CSVHandler csvHandler) {
        putDatasetInDB(csvHandler.getAccelerometerData(), Constants.RAW_ACCELEROMETER_TABLE);
        putDatasetInDB(csvHandler.getGPSData(), Constants.RAW_GPS_TABLE);
    }

    public void getProcessedData() {
        Table table = Table.loadTable(dbClient, Constants.PROCESSED_DATA_TABLE);

        if (checkTableExists(Constants.PROCESSED_DATA_TABLE)) {

        }
        else {
            System.out.println("Accelerometer table not found.");
        }
    }

    public void getAccelerometerData() {
        Table table = Table.loadTable(dbClient, Constants.PROCESSED_DATA_TABLE);

        ScanOperationConfig scanConfig = new ScanOperationConfig();
        List<String> attributeList = new ArrayList<>();
        attributeList.add(Constants.LATITUDE);
        attributeList.add(Constants.LONGITUDE);
        attributeList.add(Constants.ACCELERATION);
        scanConfig.withAttributesToGet(attributeList);

        Globals.getInstance().sensorData = table.scan(scanConfig).getAllResults();
    }

}
