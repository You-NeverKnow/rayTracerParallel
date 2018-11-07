package miscellaneous;

import World.WorldObject;
import edu.rit.image.Color;

public class IntersectionData {
    public boolean hit;
    public double distance;
    public Vector intersectionPoint = new Vector();
    public Vector normal = new Vector();
    public Color color;
    public Vector[] lights;
    public Vector intersectionDirection = new Vector();

    //technically, each object should have a different specular color
    // But white specular highlight for each object is good enough
    public WorldObject hitObject;

    public IntersectionData() {
        this.hit = false;
    }

    public IntersectionData(double distance, Vector intersectionPoint,
                            Vector intersectionDirection,
                            Vector normal, Color color, WorldObject hitObject) {
        this.hit = true;
        this.distance = distance;
        this.intersectionPoint.set(intersectionPoint);
        this.intersectionDirection.set(intersectionDirection);
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
        this.intersectionDirection.set(intersectionData.intersectionDirection);
        this.normal.set(intersectionData.normal);
        this.color = new Color(intersectionData.color);
        this.hitObject = intersectionData.hitObject;

    }
}
