package World;

public class World {
    public WorldObject[] worldObjects;
    public WorldObject[] triangleLights;

    public void transform(double[][] transformMatrix) {
        for (WorldObject worldObject: worldObjects) {
            worldObject.transform(transformMatrix);
            System.out.println(worldObject);
        }


    }
}
