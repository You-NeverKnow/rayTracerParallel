package misc;

import World.WorldObject;
import edu.rit.image.Color;

public class IntersectionData {
    public boolean hit;
    public double distance;
    public Vector intersectionPoint = new Vector();
    public Vector normal = new Vector();
    public Color color;
    public WorldObject hitObject;

    public IntersectionData() {
        this.hit = false;
    }

    public IntersectionData(double distance, Vector intersectionPoint,
                            Vector normal, Color color, WorldObject hitObject) {
        this.hit = true;
        this.distance = distance;
        this.intersectionPoint.set(intersectionPoint);
        this.normal.set(normal);
        this.color = new Color(color);
        this.hitObject = hitObject;
    }

    @Override
    public String toString() {
        return "IntersectionData{" +
                "hit = " + hit +
                ", \ndistance =" + distance +
                ", \nintersectionPoint=" + intersectionPoint +
                ", \nnormal=" + normal +
                ", \ncolor=" + color +
                ", \nhitObject=" + hitObject +
                '}';
    }

    public void set(IntersectionData intersectionData) {
        this.hit = intersectionData.hit;
        this.distance = intersectionData.distance;
        this.intersectionPoint.set(intersectionData.intersectionPoint);
        this.normal.set(intersectionData.normal);
        this.color = new Color(intersectionData.color);
        this.hitObject = intersectionData.hitObject;

    }
}
