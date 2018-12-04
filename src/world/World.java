package world;

public class World {
    public WorldObject[] worldObjects;
    public WorldObject[] triangleLights;

    public World() {
        worldObjects = new WorldObject[10];
        triangleLights = new WorldObject[2];
    }

    public void transform(double[][] transformMatrix) {
        for (WorldObject worldObject: worldObjects) {
            worldObject.transform(transformMatrix);
        }
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < worldObjects.length; i++) {
            res += worldObjects[i].toString() + "\n";
        }

        return res;
    }
}
