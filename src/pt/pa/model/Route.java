package pt.pa.model;

import java.util.Objects;

public class Route {
    private Stop orig;
    private Stop dest;
    private int distance;
    private int duration;

    public Route(Stop orig, Stop dest, int distance, int duration) {
        this.orig = orig;
        this.dest = dest;
        this.distance = distance;
        this.duration = duration;
    }

    public Stop getOrig() {
        return orig;
    }

    public Stop getDest() {
        return dest;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return orig.equals(route.getOrig()) && dest.equals(route.getDest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(orig, dest, distance, duration);
    }

    @Override
    public String toString() {
        return "Route from " + orig +
                " to " + dest +
                ", distance: " + distance +
                ", duration: " + duration;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean checkRoute(Stop orig, Stop dest){
        return orig.equals(orig) && dest.equals(dest);
    }
}
