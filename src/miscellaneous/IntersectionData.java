package miscellaneous;

public class IntersectionData {
    public boolean hit;
    public double distance;

    public IntersectionData() {
        this.hit = false;
        this.distance = 0;
    }

    public IntersectionData(boolean hit, double distance) {
        this.hit = hit;
        this.distance = distance;
    }
}
