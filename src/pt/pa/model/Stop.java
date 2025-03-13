package pt.pa.model;

import java.util.Objects;

public class Stop{
    private String code;
    private String stop;
    private double latitude;
    private double longitude;

    public Stop(String code, String stop, double latitude, double longitude) {
        this.code = code;
        this.stop = stop;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCode() {
        return code;
    }

    public String getStop() {
        return stop;
    }

    public boolean checkStop(String code){
        return this.getCode().equals(code);
    }
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stop stop1 = (Stop) o;
        return Double.compare(stop1.latitude, latitude) == 0 && Double.compare(stop1.longitude, longitude) == 0 && code.equals(stop1.getCode()) && stop.equals(stop1.getStop());
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, stop, latitude, longitude);
    }

    @Override
    public String toString() {
        return  code;
    }
}
