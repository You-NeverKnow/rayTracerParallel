import World.World;
import World.Triangle;
import World.WorldObject;
import edu.rit.image.Color;
import edu.rit.image.ColorArray;
import edu.rit.image.ColorImageQueue;
import edu.rit.image.ColorPngWriter;
import miscellaneous.IntersectionData;
import miscellaneous.Ray;
import miscellaneous.Vector;

import java.io.*;

public class Camera {
    Vector eyePoint;
    Vector lookAt;
    Vector up;
    int focalLength;

    // For writing PNG image file.
    ColorPngWriter writer;
    ColorImageQueue imageQueue;
    ColorArray pixelRow;
    File filename;

    public Camera(Vector eyePoint, Vector lookAt, Vector up, int focalLength) {
        this.eyePoint = eyePoint;
        this.lookAt = lookAt;
        this.up = up;
        this.focalLength = focalLength;
        this.filename = new File("../output/renderedImage.png");
    }

    public void render(World world, int width, int height)
            throws InterruptedException, IOException {

        Vector n = eyePoint.subtract(lookAt);

        // Normalize vectors
        n.normalize();
        up.normalize();

        // define u, v
        Vector u = up.cross(n);
        Vector v = n.cross(u);

        // Define transform matrix to convert to camera coordinates
        double[][] transformMatrix = new double[][] {
            {u.x, v.x, n.x, 0},
            {u.y, v.y, n.y, 0},
            {u.z, v.z, n.z, 0},
            {-this.eyePoint.dot(u),
                    -this.eyePoint.dot(v), -this.eyePoint.dot(n), 1}

        };
        world.transform(transformMatrix);


        // Set up file for writing image
        writer = new ColorPngWriter (height, width, new BufferedOutputStream(
                                            new FileOutputStream(filename)));
        filename.setReadable(true, false);
        filename.setWritable(true, false);
        imageQueue = writer.getImageQueue();
        pixelRow = new ColorArray (width);

        // Init projection plane
        Vector imageOrigin = new Vector(-width/2, -height/2,
                this.focalLength);

        Ray ray = new Ray();
        Vector rayOrigin = new Vector(0, 0, 0);
        ray.origin.set(rayOrigin);

        Vector pixelPosition = new Vector(0, 0, this.focalLength);

        // Iterate over all pixels, and compute radiance
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                // Get current pixel position
                pixelPosition.x = imageOrigin.x + col;
                pixelPosition.y = imageOrigin.y + row;
                pixelPosition.z = this.focalLength;

                pixelPosition.normalize();

                // Init ray
                ray.direction.set(pixelPosition);

                // Get color for pixel
                pixelRow.color(col, getRadiance(ray, world));
            }
            imageQueue.put(imageQueue.rows() - 1 - row, pixelRow);
        }

        writer.write();
    }

    /*
    Returns color of the object that the ray hits
    */
    private Color getRadiance(Ray ray, World world) {
        WorldObject hitObject = getHitObject(ray, world);
        return hitObject.color;
    }

    /*
    Returns a reference to the worldObject that got hit by the ray
    */
    private WorldObject getHitObject(Ray ray, World world) {
        double distance = Double.MAX_VALUE;
        WorldObject hitObject = new Triangle();

        // Check intersection with all objects
        for (WorldObject worldObject: world.worldObjects) {
            IntersectionData intersectionData = worldObject.intersect(ray);
            // If this ray intersects with this object and its distance of hit
            // is smaller than previous found, remember it
            if (intersectionData.hit && intersectionData.distance < distance) {
                hitObject = worldObject;
            }
        }

        return hitObject;
    }


}
