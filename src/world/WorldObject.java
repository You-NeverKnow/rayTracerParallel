package world;

import edu.rit.image.Color;
//import edu.rit.io.Streamable;
import misc.IntersectionData;
import misc.Ray;

import java.io.Serializable;

/**
 * world
 */
public abstract class WorldObject implements Serializable {
    public Color color = new Color().rgb(0);
    public abstract void transform(double[][] transformMatrix);
    public abstract IntersectionData intersect(Ray ray);

    // Every world object must have a bounding box
    public abstract void setBoundingBox();
    public abstract  misc.BoundingBox getBoundingBox();

    // gets the vector of first corner stored in bounding box.
    // of a world object
    public abstract misc.Vector getFirstCorner();
}