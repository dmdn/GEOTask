package by.ddv.geotask.json.model_geocoding;


import java.util.List;


public class GoogleGeocoding {

    private List<Results> results;
    private String status;

    public GoogleGeocoding() {
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
