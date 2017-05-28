package by.ddv.geotask.json.model_directions;


import java.util.List;



public class GoogleDirections {

    private List<Routes> routes;
    private String status;

    public GoogleDirections(List<Routes> routes, String status) {
        this.routes = routes;
        this.status = status;
    }

    public List<Routes> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
