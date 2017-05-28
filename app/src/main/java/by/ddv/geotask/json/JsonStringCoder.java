package by.ddv.geotask.json;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class JsonStringCoder {

    //Converting the input string for the http request

    public static String getJsonString(String input) {

        String trimString = input.trim().replace(" ", "+");

        String encodeString = null;

        try {
            encodeString = URLEncoder.encode(trimString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodeString;
    }


}
