package World;

public class World {
    public WorldObject[] worldObjects;
    public Light[] lights;

    public void transform(double[][] transformMatrix) {
        for (WorldObject worldObject: worldObjects) {
            worldObject.transform(transformMatrix);
        }
    }
}
