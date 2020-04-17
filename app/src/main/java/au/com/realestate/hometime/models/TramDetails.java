package au.com.realestate.hometime.models;

public class TramDetails {

    private String time;
    private String routeNum;
    private String vehicleNum;
    private String destination;

    public TramDetails(String time, String routeNum, String vehicleNum, String destination) {
        this.setTime(time);
        this.setRouteNum(routeNum);
        this.setVehicleNum(vehicleNum);
        this.setDestination(destination);
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
