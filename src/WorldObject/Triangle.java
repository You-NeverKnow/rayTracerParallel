package WorldObject;

import miscellaneous.Ray;
import miscellaneous.Vector;

/**
 * Triangle
 */
public class Triangle extends WorldObject {
    Triangle (Vector vertex1, Vector vertex2, Vector vertex3) {
        vertex1 = new Vector(vertex1.x, vertex1.y, vertex1.z);
        vertex2 = new Vector(vertex2.x, vertex2.y, vertex2.z);
        vertex3 = new Vector(vertex3.x, vertex3.y, vertex3.z);
    }

    public void intersect(Ray ray) {
        // Ray = origin --> Vector, direction-->Vector(always normalized)

    }
    
}