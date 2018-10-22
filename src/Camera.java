import WorldObject.WorldObject;
import edu.rit.image.ColorArray;
import edu.rit.image.ColorImageQueue;
import edu.rit.image.ColorPngWriter;
import miscellaneous.Vector;

public class Camera {
    Vector eyePoint;
    Vector lookAt;
    Vector up;
    int focalLength;

    // For writing PNG image file.
    ColorPngWriter writer;
    ColorImageQueue imageQueue;
    ColorArray pixelData;

    public Camera(Vector position, Vector look_at, int focalLength) {

    }
    public void render(WorldObject[] worldObjects, int height, int width) throws InterruptedException {
        Vector n = this.eyePoint.subtract(this.lookAt);
        Vector up = this.up;

        // Normalize vectors
        n.normalize();
        up.normalize();

        // define u, v
        Vector u = up.cross(n);
        Vector v = n.cross(u);

        // Define transform matrix to convert to camera coordinates
        float[][] transformMatrix = new float[][] {
            {u.x, v.x, n.x, 0},
            {u.y, v.y, n.y, 0},
            {u.z, v.z, n.z, 0},
            {-this.eyePoint.dot(u),
                    -this.eyePoint.dot(v), -this.eyePoint.dot(n), 1}
        };

        Vector imageOrigin = new Vector(-width/2, -height/2,
                                                this.focalLength);

        Vector rayOrigin = new Vector(0, 0, 0);
        Vector pixelPosition = new Vector(0, 0, this.focalLength);


        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pixelPosition.x = imageOrigin.x + col;
                pixelPosition.y = imageOrigin.y + row;

                pixelPosition.normalize();
                pixelData.color(col, getRadiance());
            }
            imageQueue.put(row, pixelData);
        }
    }
}
