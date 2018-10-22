package WorldObject;

import miscellaneous.Matrix;
import miscellaneous.Ray;
import miscellaneous.Vector;

/**
 * WorldObject
 */
public class WorldObject {
    Vector position;
    Vector color;

    public Vector transform(Matrix transformMatrix) {
//        return transformMatrix.matmul(position);
    }
    
    public IntersectionObject intersect(Ray ray);
}