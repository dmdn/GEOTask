package by.ddv.geotask.list_view;



public class GeocogingListItem {

    private String address;
    private String coordinates;
    private double lat;
    private double lng;

    public GeocogingListItem(String address, String coordinates, double lat, double lng) {
        this.address = address;
        this.coordinates = coordinates;
        this.lat = lat;
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
