import edu.rit.pj2.Task;
import edu.rit.image.Color;
import edu.rit.image.ColorArray;
import edu.rit.image.ColorImageQueue;
import edu.rit.image.ColorPngWriter;
import edu.rit.util.Random;
import World.World;
import World.WorldObject;
import World.World;
import World.Sphere;
import World.Triangle;
import misc.IntersectionData;
import misc.Ray;
import misc.Vector;
import misc.KDTreeWO;

import java.io.*;

public class RayTraceSeq extends Task{
	Vector eyePoint;
	Vector lookAt;
	Vector up;
	int focalLength;

	// For writing PNG image file.
	ColorPngWriter writer;
	ColorImageQueue imageQueue;
	ColorArray pixelRow;
	File filename;

	public void setCameraSeq(Vector eyePoint, Vector lookAt, Vector up, int focalLength) {
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

		//KDTreeWO objects = new KDTreeWO(world.worldObjects);

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
			if (hitData.hitObject == world.triangleLights[0] ||
					hitData.hitObject == world.triangleLights[1]) {
				return hitData.color;
			}
			return getIllumination(hitData, world, sampler);
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

	private static void create_scene(World world) {
		world.worldObjects = new WorldObject[14];

		int x1, x2, y1, y2, z1, z2;
		Vector vertex0, vertex1, vertex2, vertex3;

		x1 = 75 - 540;
		x2 = 75 + 540;
		y1 = 60 - 540;
		y2 = 60 + 540;
		z1 = -540;
		z2 = -1400;


		// Floor
		vertex0 = new Vector(x1, y1, z1);
		vertex1 = new Vector(x1, y1, z2);
		vertex2 = new Vector(x2, y1, z1);
		vertex3 = new Vector(x2, y1, z2);

		Triangle halfFloor1 = new Triangle(vertex1, vertex0, vertex2);
		Triangle halfFloor2 = new Triangle(vertex1, vertex2, vertex3);
		halfFloor1.color.rgb(1f, 0.0f, 0.0f);
		halfFloor2.color.rgb(1f, 0.0f, 0.0f);

		world.worldObjects[0] = halfFloor1;
		world.worldObjects[1] = halfFloor2;


		// Ceiling
		vertex0 = new Vector(x1, y2, z1);
		vertex1 = new Vector(x1, y2, z2);
		vertex2 = new Vector(x2, y2, z1);
		vertex3 = new Vector(x2, y2, z2);

		Triangle halfCeiling1 = new Triangle(vertex0, vertex1, vertex2);
		Triangle halfCeiling2 = new Triangle(vertex2, vertex1, vertex3);
		halfCeiling1.color.rgb(1f, 0.0f, 0.0f);
		halfCeiling2.color.rgb(1f, 0.0f, 0.0f);

		world.worldObjects[2] = halfCeiling1;
		world.worldObjects[3] = halfCeiling2;


		// Left side wall
		vertex0 = new Vector(x1, y1, z1);
		vertex1 = new Vector(x1, y1, z2);
		vertex2 = new Vector(x1, y2, z1);
		vertex3 = new Vector(x1, y2, z2);

		Triangle halfLeftWall1 = new Triangle(vertex0, vertex1, vertex2);
		Triangle halfLeftWall2 = new Triangle(vertex2, vertex1, vertex3);
		halfLeftWall1.color.rgb(0.59f, 0.0f, 0.0f);
		halfLeftWall2 .color.rgb(0.59f, 0.0f, 0.0f);

		world.worldObjects[4] = halfLeftWall1;
		world.worldObjects[5] = halfLeftWall2;

		// Right side wall
		vertex0 = new Vector(x2, y1, z1);
		vertex1 = new Vector(x2, y1, z2);
		vertex2 = new Vector(x2, y2, z1);
		vertex3 = new Vector(x2, y2, z2);

		Triangle halfRightWall1 = new Triangle(vertex1, vertex0, vertex2);
		Triangle halfRightWall2 = new Triangle(vertex1, vertex2, vertex3);
		halfRightWall1.color.rgb(0.59f, 0.0f, 0.0f);
		halfRightWall2 .color.rgb(0.59f, 0.0f, 0.0f);

		world.worldObjects[6] = halfRightWall1;
		world.worldObjects[7] = halfRightWall2;

		// Back wall
		vertex0 = new Vector(x1, y1, z2);
		vertex1 = new Vector(x1, y2, z2);
		vertex2 = new Vector(x2, y1, z2);
		vertex3 = new Vector(x2, y2, z2);

		Triangle halfBackWall1 = new Triangle(vertex1, vertex0, vertex2);
		Triangle halfBackWall2 = new Triangle(vertex1, vertex2, vertex3);
		halfBackWall1.color.rgb(0.49f, 0.0f, 0.0f);
		halfBackWall2.color.rgb(0.49f, 0.0f, 0.0f);

		world.worldObjects[8] = halfBackWall1;
		world.worldObjects[9] = halfBackWall2;

		// Sphere 1
		double radius;

		radius = 120;
		Vector center = new Vector(x1 + radius + 150, y1 + radius, z2 + radius + 300);
		Sphere sphere1 = new Sphere(radius, center);
		sphere1.color.rgb(0.0f, 1f, 0.0f);

		world.worldObjects[10] = sphere1;

		// Sphere 2
		radius = 120;
		center = new Vector(x2 - radius - 100, y1 + radius, z1 - radius - 100);
		Sphere sphere2 = new Sphere(radius, center);
		sphere2.color.rgb(0.0f, 1f, 0.0f);

		world.worldObjects[11] = sphere2;

		// Add lights
		vertex0 = new Vector(75 - 150, y2 - 1e-12, -850);
		vertex1 = new Vector(75 - 150, y2 - 1e-12, -1100);
		vertex2 = new Vector(75 + 150, y2 - 1e-12, -850);
		vertex3 = new Vector(75 + 150, y2 - 1e-12, -1100);

		Triangle light1 = new Triangle(vertex1, vertex0, vertex2);
		Triangle light2 = new Triangle(vertex1, vertex2, vertex3);
		light1.color.rgb(1f, 1f, 1f);
		light2.color.rgb(1f, 1f, 1f);

		world.worldObjects[12] = light1;
		world.worldObjects[13] = light2;

		// Light objects add
		world.triangleLights = new WorldObject[2];

		world.triangleLights[0] = light1;
		world.triangleLights[1] = light2;

	}

	public void main(String[] args) throws Exception {

		World world = new World();
		create_scene(world);

		eyePoint = new Vector(75, 60, 540);
		lookAt = new Vector(75, 60, 530);
		up = new Vector(0, 1, 0);
		focalLength = 1080;
		filename = new File("../output/renderedImage.png");
		
		//setCameraSeq(eyePoint, lookAt, up, focalLength);

		render(world, 1080, 1080);
	}


}
