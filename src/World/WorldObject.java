package World;

import edu.rit.image.Color;
import miscellaneous.IntersectionData;
import miscellaneous.Phong;
import miscellaneous.Ray;

/**
 * World
 */
public abstract class WorldObject {
    public Color color = new Color().rgb(0);
    public Phong phong = new Phong();

    public abstract void transform(double[][] transformMatrix);
    public void setPhongModel(Phong phong) {
        this.phong.set(phong);
    }
    public abstract IntersectionData intersect(Ray ray);
}