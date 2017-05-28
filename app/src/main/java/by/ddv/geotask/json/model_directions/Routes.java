package by.ddv.geotask.json.model_directions;


import java.util.List;

public class Routes {

    Overview_polyline overview_polyline;
    private List<Legs> legs;


    public Routes(Overview_polyline overview_polyline, List<Legs> legs) {
        this.overview_polyline = overview_polyline;
        this.legs = legs;
    }

    public Overview_polyline getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(Overview_polyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }

    public List<Legs> getLegs() {
        return legs;
    }

    public void setLegs(List<Legs> legs) {
        this.legs = legs;
    }
}
