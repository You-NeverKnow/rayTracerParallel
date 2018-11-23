package misc;

public class Photon {
    static double intensity;

    Vector position;

    public Photon(Vector position) {
        this.position = position;
    }

    public String toString() {
        return position.toString();
    }

}
