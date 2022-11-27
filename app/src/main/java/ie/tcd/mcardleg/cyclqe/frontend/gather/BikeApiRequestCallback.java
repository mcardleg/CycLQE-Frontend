package ie.tcd.mcardleg.cyclqe.frontend.gather;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ie.tcd.mcardleg.cyclqe.frontend.Constants;
import ie.tcd.mcardleg.cyclqe.frontend.Globals;

public class BikeApiRequestCallback extends UrlRequest.Callback {
    String responseBodyString;

    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
        System.out.println("onRedirectReceived method called.");
        request.cancel();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
        System.out.println("onResponseStarted method called.");
        responseBodyString = "";
        request.read(ByteBuffer.allocateDirect(102400));
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
        System.out.println("onReadCompleted method called.");

        byteBuffer.flip();
        CharBuffer cb = byteBuffer.asCharBuffer();
        responseBodyString += Charset.forName(StandardCharsets.UTF_8.name()).decode(byteBuffer).toString();
        byteBuffer.clear();
        request.read(byteBuffer);
    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
        System.out.println("onSucceeded method called.");
        List<BikeLocation> bikeLocations = new ArrayList<BikeLocation>();

        try {
            JSONObject object = new JSONObject(responseBodyString);
            JSONArray bikeSummaries = object.getJSONObject(
                    Constants.DATA_JSON_FIELD).getJSONArray(Constants.BIKE_JSON_FIELD);

            for(int n = 0; n < bikeSummaries.length(); n++) {
                Double latitude = bikeSummaries
                        .getJSONObject(n)
                        .getDouble(Constants.LATITUDE_JSON_FIELD);
                Double longitude = bikeSummaries
                        .getJSONObject(n)
                        .getDouble(Constants.LONGITUDE_JSON_FIELD);
                BikeLocation bikeLocation = new BikeLocation(latitude, longitude);
                bikeLocations.add(bikeLocation);
            }

            Globals.getInstance().bikeLocations = bikeLocations;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
        // The request has failed. If possible, handle the error.
        System.err.println("The request failed: " + error.getMessage());
    }
}
