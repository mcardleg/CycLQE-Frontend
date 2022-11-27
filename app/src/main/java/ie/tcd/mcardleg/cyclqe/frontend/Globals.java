package ie.tcd.mcardleg.cyclqe.frontend;


import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;

import java.util.ArrayList;
import java.util.List;

import ie.tcd.mcardleg.cyclqe.frontend.gather.BikeLocation;

public class Globals {

    private static Globals instance = null;

    public List<BikeLocation> bikeLocations = new ArrayList<BikeLocation>();
    public List<Document> sensorData = new ArrayList<Document>();

    protected Globals(){}

    public static synchronized Globals getInstance() {
        if(null == instance){
            instance = new Globals();
        }
        return instance;
    }

}
