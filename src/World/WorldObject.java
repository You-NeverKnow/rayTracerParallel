package World;

import edu.rit.image.Color;
import miscellaneous.IntersectionData;
import miscellaneous.Ray;

/**
 * World
 */
public abstract class WorldObject {
    public Color color = new Color().rgb(0);
    public abstract void transform(double[][] transformMatrix);
    public abstract IntersectionData intersect(Ray ray);
}