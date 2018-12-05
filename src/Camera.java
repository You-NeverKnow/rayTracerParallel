import World.World;
import World.WorldObject;
import edu.rit.image.Color;
import edu.rit.image.ColorArray;
import edu.rit.image.ColorImageQueue;
import edu.rit.image.ColorPngWriter;
import edu.rit.util.Random;
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
        double projectionZ = n.multiply(-focalLength).z;
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
        Vector imageOrigin = new Vector(-540, -540, projectionZ);

        Ray ray = new Ray();
        Vector pixelPosition = new Vector(0, 0, projectionZ);
        Random sampler = new Random(42);

        // Max color --> multiplier must be equal to color of light source
        // in getRadiance --> = nSamples
        Vector maxColor = new Vector(1, 1, 1).multiply(55);

        // Iterate over all pixels, and compute radiance
        double widthFactor = width/1080.0;
        double heightFactor = height/1080.0;

        for (int row = 0; row < height; row ++) {
            for (int col = 0; col < width; col ++) {

                // Get current pixel position
                pixelPosition.x = imageOrigin.x + col/heightFactor;
                pixelPosition.y = imageOrigin.y + row/widthFactor;
                pixelPosition.z = projectionZ;

                ray.origin.set(pixelPosition);
                pixelPosition.normalize();

                // Init ray
                ray.direction.set(pixelPosition);

                // Get color for pixel
                pixelRow.color(col,
                        Vector._convertVectorColor( getRadiance(ray, world,
                                            sampler, 3), maxColor));
            }
            imageQueue.put(imageQueue.rows() - 1 - row, pixelRow);
        }

        writer.write();
    }

    /*
    Returns color of the object that the ray hits
    */
    private Vector getRadiance(Ray ray, World world, Random sampler, int depth) {
        IntersectionData hitData = getHitData(ray, world);
        // TODO: ADD shadow ray and shading here

        if (hitData.hit) {
            // Point is inside light source
            if (hitData.hitObject == world.triangleLights[0] ||
                    hitData.hitObject == world.triangleLights[1]) {
                return new Vector(1f, 1f, 1f).multiply(55);
            }
            // Point is outside light source, somewhere in scene
            adjustColorWRTLightSource(hitData, world, sampler);

            Vector localIllumination =
                                hitData.hitObject.phong.computeRadiance(hitData);

            if (depth > 0 )
                localIllumination.selfAdd((rayTraceRecursive(hitData,
                                                    world, sampler, depth)));
            return localIllumination;
        }
        else {

            return new Vector();
        }
    }

    private Vector rayTraceRecursive(IntersectionData hitData,
                                     World world, Random sampler, int depth) {
        Vector illumination = new Vector();
        double kr = hitData.hitObject.phong.kr;
        double kt = hitData.hitObject.phong.kt;
        Vector s = hitData.intersectionDirection.multiply(1);


        // Add reflective radiance
        if (kr > 0) {
            Vector r = Vector._getReflectedRay(s, hitData.normal);
            r.normalize();
            Ray reflectedRay = new Ray(hitData.intersectionPoint, r);

            illumination.selfAdd(getRadiance(reflectedRay, world, sampler,
                                                depth-1).multiply(kr));
        }
        // Add transmitive radiance
        if (kt > 0) {
            // assuming light transport medium is air
            double relativeRefractionIndex = hitData.hitObject.refractiveIndex;
            Vector refractedRayDirection = Vector._getRefractedRay(
                                                    s, hitData.normal,
                            1/relativeRefractionIndex);

            // Total internal reflection
            if (refractedRayDirection == null) {
                Vector r = Vector._getReflectedRay(s, hitData.normal);
                r.normalize();
                Ray reflectedRay = new Ray(hitData.intersectionPoint, r);
                illumination.selfAdd(getRadiance(reflectedRay, world, sampler,
                                                depth-1).multiply(kt));

            } else {
                Ray refractedRay = new Ray(hitData.intersectionPoint.add(
                        hitData.normal.multiply(-1e-9)), refractedRayDirection);
                IntersectionData internalHitData =
                        hitData.hitObject.intersect(refractedRay);

                refractedRayDirection = Vector._getRefractedRay(
                                            refractedRayDirection,
                                            internalHitData.normal.multiply(-1),
                                            relativeRefractionIndex);

                refractedRay.origin.set(internalHitData.intersectionPoint);
                refractedRay.direction.set(refractedRayDirection);
                illumination.selfAdd(getRadiance(refractedRay, world, sampler,
                                                depth-1).multiply(kt));

            }
        }
        return illumination;

    }

    private void adjustColorWRTLightSource(IntersectionData hitData, World world, Random sampler) {

        Vector lightSamplePoint = new Vector();
        Ray shadowRay = new Ray();

        shadowRay.origin.set(hitData.intersectionPoint.add(
                                            hitData.normal.multiply(1e-10)));
        Vector shadowRayDirection;
        double x, z;
        int lightHitCounter = 0;
        int nSamples = 50;
        hitData.lights = new Vector[nSamples];

        for (int i = 0; i < nSamples; i++) {

            x = sampler.nextDouble();
            z = sampler.nextDouble();

            lightSamplePoint.x = -150 + 300 * x;
            lightSamplePoint.y =  540;
            lightSamplePoint.z =  -1640 + 250 * z;

            shadowRayDirection = lightSamplePoint.subtract(shadowRay.origin);
            shadowRayDirection.normalize();
            shadowRay.direction.set(shadowRayDirection);

            IntersectionData shadowRayHitData = this.getHitData(
                                                            shadowRay, world);

            if (shadowRayHitData.hit &&
                    (shadowRayHitData.hitObject == world.triangleLights[0] ||
                    shadowRayHitData.hitObject == world.triangleLights[1])
            ) {
                lightHitCounter += 1;

                // record when light hits. rest of array remains null
                hitData.lights[i] = new Vector();
                hitData.lights[i].set(lightSamplePoint);

            }
        }

        float r = lightHitCounter * hitData.color.red();
        r /= nSamples;

        float g = lightHitCounter * hitData.color.green();
        g /= nSamples;

        float b = lightHitCounter * hitData.color.blue();
        b /= nSamples;

        hitData.color.rgb(r/256f, g/256f, b/256f);
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
