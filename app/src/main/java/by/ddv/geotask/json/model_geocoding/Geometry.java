package by.ddv.geotask.json.model_geocoding;



public class Geometry {

    private LocationObj location;


    public Geometry(LocationObj location) {
        this.location = location;
    }

    public LocationObj getLocation() {
        return location;
    }

    public void setLocation(LocationObj location) {
        this.location = location;
    }
}
