import World.World;
import World.WorldObject;
import edu.rit.image.Color;
import edu.rit.image.ColorArray;
import edu.rit.image.ColorImageQueue;
import edu.rit.image.ColorPngWriter;
import edu.rit.util.Random;
import misc.IntersectionData;
import misc.Ray;
import misc.Vector;

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
        double projectionZ = n.multiply(-focalLength).z;
        System.out.println("ProjectionZ: "+ projectionZ);
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
                projectionZ);

        Ray ray = new Ray();
        Vector pixelPosition = new Vector(0, 0, projectionZ);
        Random sampler = new Random(42);

        // Iterate over all pixels, and compute radiance
        // TODO: Multisampling / width height given in world coordinates
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                // Get current pixel position
                pixelPosition.x = imageOrigin.x + col;
                pixelPosition.y = imageOrigin.y + row;
                pixelPosition.z = projectionZ;

                ray.origin.set(pixelPosition);
                pixelPosition.normalize();

                // Init ray
                ray.direction.set(pixelPosition);

                // Get color for pixel
                pixelRow.color(col, getRadiance(ray, world, sampler));
            }
            imageQueue.put(imageQueue.rows() - 1 - row, pixelRow);
        }

        writer.write();
    }

    /*
    Returns color of the object that the ray hits
    */
    private Color getRadiance(Ray ray, World world, Random sampler) {
        IntersectionData hitData = getHitData(ray, world);
        // TODO: ADD shadow ray and shading here

        if (hitData.hit) {
            // if (hitData.hitObject == world.triangleLights[0] ||
            //         hitData.hitObject == world.triangleLights[1]) {
            //     return hitData.color;
            // }
            // return getIllumination(hitData, world, sampler);
                return hitData.color;
        }
        else {
            return new Color().rgb(0);
        }
    }

    private Color getIllumination(IntersectionData hitData, World world, Random sampler) {

        Vector lightSamplePoint = new Vector();
        Ray shadowRay = new Ray();

        shadowRay.origin.set(hitData.intersectionPoint.add(
                                            hitData.normal.multiply(1e-10)));
        Vector shadowRayDirection;
        double x, z;
        int lightHitCounter = 0;
        int nSamples = 10;
        for (int i = 0; i < nSamples; i++) {

            x = sampler.nextDouble();
            z = sampler.nextDouble();

            lightSamplePoint.x = -150 + 300 * x;
            lightSamplePoint.y =  540;
            lightSamplePoint.z =  -1640 + 250 * z;

            shadowRayDirection = lightSamplePoint.subtract(shadowRay.origin);
            shadowRayDirection.normalize();
            shadowRay.direction.set(shadowRayDirection);

            IntersectionData shadowRayHitData = this.getHitData(shadowRay, world);

            if (shadowRayHitData.hit &&
                    (shadowRayHitData.hitObject == world.triangleLights[0] ||
                    shadowRayHitData.hitObject == world.triangleLights[1])
            ) {
                lightHitCounter += 1;

            }

        }

        float r = lightHitCounter * hitData.color.red();
        r /= nSamples;

        float g = lightHitCounter * hitData.color.green();
        g /= nSamples;

        float b = lightHitCounter * hitData.color.blue();
        b /= nSamples;

        hitData.color.rgb(r/256f, g/256f, b/256f);
        return hitData.color;
    }

    /*
    Returns a reference to the worldObject that got hit by the ray
    */
    private IntersectionData getHitData(Ray ray, World world) {
        double distance = Double.MAX_VALUE;
        IntersectionData hitData = new IntersectionData();
        // Check intersection with all objects
        // TODO: Add kd-tree here
        for (WorldObject worldObject: world.worldObjects) {
            IntersectionData intersectionData = worldObject.intersect(ray);
            // If this ray intersects with this object and its distance of hit
            // is smaller than previous found, remember it
            if (intersectionData.hit && intersectionData.distance < distance) {
                distance = intersectionData.distance;
                hitData.set(intersectionData);
            }
        }
        return hitData;
    }


}
