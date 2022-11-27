package ie.tcd.mcardleg.cyclqe.frontend.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.chromium.net.CronetEngine;

import java.util.concurrent.Executors;

import ie.tcd.mcardleg.cyclqe.frontend.Constants;
import ie.tcd.mcardleg.cyclqe.frontend.R;
import ie.tcd.mcardleg.cyclqe.frontend.collect.CSVHandler;
import ie.tcd.mcardleg.cyclqe.frontend.gather.BikeApiRequestCallback;
import ie.tcd.mcardleg.cyclqe.frontend.gather.DBController;

public class MainActivity extends AppCompatActivity {

    private final Context context = MainActivity.this;

    private Button collectButton;
    private Button gatherButton;
    private Button bikeDataButton;
    private Button getDataButton;
    private Button mapButton;
    private CSVHandler csvHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        collectButton = (Button) findViewById(R.id.collect_data_button);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Retrieving data.", Toast.LENGTH_SHORT).show();
                csvHandler = new CSVHandler(context);
                System.out.println("Parsed CSV data.");
                Toast.makeText(context, "Data parsed.", Toast.LENGTH_SHORT).show();
            }
        });

        gatherButton = (Button) findViewById(R.id.gather_data_button);
        gatherButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Sending data to database.", Toast.LENGTH_SHORT).show();
                DBController dbController = new DBController();
                dbController.putDatasetsInDB(csvHandler);
                System.out.println("Data sent to database.");
                Toast.makeText(context, "Data sent to the database.", Toast.LENGTH_SHORT).show();
            }
        });

        bikeDataButton = (Button) findViewById(R.id.getBikeData);
        bikeDataButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Toast.makeText(context,
                        "Getting data from Bleeper API.",
                        Toast.LENGTH_SHORT).show();
                CronetEngine cronetEngine = new CronetEngine
                        .Builder(context).build();

                cronetEngine
                        .newUrlRequestBuilder(Constants.BLEEPER_API_URL,
                                new BikeApiRequestCallback(),
                                Executors.newSingleThreadExecutor())
                        .build()
                        .start();
                Toast.makeText(context,
                        "Bleeper bike data received from the open API.",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        getDataButton = (Button) findViewById(R.id.getFromDB);
        getDataButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Getting data from database.", Toast.LENGTH_SHORT).show();
                DBController dbController = new DBController();
                dbController.getAccelerometerData();
                System.out.println("Data received from database.");
                Toast.makeText(context, "Cycle lane quality data received from the database.", Toast.LENGTH_SHORT).show();
            }
        });

        mapButton = (Button) findViewById(R.id.showMap);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MapsActivity.class));
            }
        });
    }
}