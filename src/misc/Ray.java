package misc;

public class Ray {
    public Vector origin = new Vector();
    public Vector direction = new Vector();

    public Ray() {}

    public Ray(Vector origin, Vector direction) {
        this.origin.set(origin);
        this.direction.set(direction);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "origin=" + origin +
                ", direction=" + direction +
                '}';
    }
}
