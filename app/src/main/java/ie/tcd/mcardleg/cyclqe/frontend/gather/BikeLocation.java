package ie.tcd.mcardleg.cyclqe.frontend.gather;

public class BikeLocation {

    private Double latitude;
    private Double longitude;

    public BikeLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
