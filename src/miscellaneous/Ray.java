package miscellaneous;

public class Ray {
    public Vector origin = new Vector();
    public Vector direction = new Vector();

    public Ray() {}

    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }
}
