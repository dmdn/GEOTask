package by.ddv.geotask.json;


import android.support.annotation.NonNull;



public class Common {

    //https://maps.googleapis.com/maps/api/geocode/json?address=Bre&key=AIzaSyCDEmlKTB6GhQvDsfa9JmCWr3wLcMFQlNA

    //https://maps.googleapis.com/maps/api/directions/json?origin=53.9045,27.5615&destination=53.6694,23.8131&key=AIzaSyAVp3wI0lb8K2-S1U5s4DvEN317bsMIMwI

    public static String API_LINK_GEOCODING = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    public static String API_KEY_GEOCODING = "AIzaSyCDEmlKTB6GhQvDsfa9JmCWr3wLcMFQlNA";

    public static String API_LINK_DIRECTION = "https://maps.googleapis.com/maps/api/directions/json?origin=";
    public static String API_KEY_DIRECTION = "AIzaSyAVp3wI0lb8K2-S1U5s4DvEN317bsMIMwI";

    @NonNull
    public static String apiRequestGeocoding(String address){
        StringBuilder sb = new StringBuilder(API_LINK_GEOCODING);
        sb.append(String.format("%s&key=%s", JsonStringCoder.getJsonString(address), API_KEY_GEOCODING));
        return sb.toString();
    }

    @NonNull
    public static String apiRequestDirections(String latForStr, String lngForStr, String latToStr, String lngToStr){
        StringBuilder sb = new StringBuilder(API_LINK_DIRECTION);

        sb.append(String.format("%s,%s&destination=%s,%s&key=%s", latForStr, lngForStr, latToStr, lngToStr, API_KEY_DIRECTION));
        return sb.toString();
    }

}
