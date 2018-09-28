/**
 * WorldObject
 */
public class WorldObject {
    Vector position;
    Vector color;

    public Vector transform(Matrix transformMatrix) {
        Vector transformedPosition;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; row < 4; row++) {
                for (int k = 0; k < 1; k++) {
                    transformedPosition[]
                }
            }    
        }
    }
    
    public IntersectionObject intersect(Ray ray);
}