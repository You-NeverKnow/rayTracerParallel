package world;

public class World {
    public WorldObject[] worldObjects;
    public WorldObject[] triangleLights;
    public WorldObject[] debugLights;

    public void transform(double[][] transformMatrix) {
        for (WorldObject worldObject: worldObjects) {
            worldObject.transform(transformMatrix);
        }


    }
}
