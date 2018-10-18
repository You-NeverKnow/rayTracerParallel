import miscellaneous.Vector;

/**
 * Camera
 */
public class Camera {

    Camera(Vector position, Vector lookAt, int focalLength) {
        this.position = position;
        this.lookAt = lookAt;
        this.focalLength = focalLength;
    }
    
    void render(WorldObject[] worldObjects) {
        
        // Transform to camera coordinates
        for (WorldObject worldObject : worldObjects) {
            worldObject.transform();
        };

        
    }
    
}