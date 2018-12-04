import edu.rit.pj2.Job;
import edu.rit.pj2.Task;
import edu.rit.pj2.Loop;
import edu.rit.pj2.Section;
import edu.rit.pj2.Tuple;
import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.image.Color;
import edu.rit.image.ColorArray;
import edu.rit.image.ColorImageQueue;
import edu.rit.image.ColorPngWriter;
import edu.rit.util.Random;
import world.*;
import misc.IntersectionData;
import misc.Ray;
import misc.Vector;

import java.io.*;
import java.util.ArrayList;

public class RayTraceClu extends Job {

	World world;

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

	public static void loadScene(String filename,World world) {

		File f = new File(filename);
		String line;
		String elems[];
		ArrayList<WorldObject> worldObjects = new ArrayList<>(4000);
		ArrayList<Vector> positions = new ArrayList<>(4000);
		double x, y, z;
		int idx1, idx2, idx3;
		Triangle t;
		Vector v1, v2, v3;
		Color color = new Color().rgb(0,250,250);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

			while ( (line = reader.readLine()) != null) {
				System.out.println("iter");
				System.out.flush();
				elems = line.trim().split(" ");

				if (elems[0].equals("v")) {

					// v x y z
					x = Double.parseDouble(elems[1]);
					y = Double.parseDouble(elems[2]);
					z = 850 + Double.parseDouble(elems[3]);
					positions.add(new Vector(x, y, z));
				} else if (elems[0].equals("f")) {

					//f v/vt/vn v/vt/vn v/vt/vn
					//ignore vt and vn
					idx1 = Integer.parseInt(elems[1].split("/")[0])-1;
					idx2 = Integer.parseInt(elems[2].split("/")[0])-1;
					idx3 = Integer.parseInt(elems[3].split("/")[0])-1;

					worldObjects.add(new Triangle(positions.get(idx1), positions.get(idx2), positions.get(idx3), color));

				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add lights
		v1 = new Vector(75 - 150, 60+540 - 1e-12, -1100);
		v2 = new Vector(75 + 150, 60+540 - 1e-12, -850);
		v3 = new Vector(75 + 150, 60+540 - 1e-12, -1100);

		Triangle light = new Triangle(v1, v2, v3);
		light.color.rgb(1f, 1f, 1f);

		worldObjects.add(light);

		// Light objects add
		world.triangleLights = new WorldObject[1];

		world.triangleLights[0] = light;
		world.worldObjects = new WorldObject[worldObjects.size()];
		world.worldObjects = worldObjects.toArray(world.worldObjects);

	}

	public void main(String args[]) throws Exception{

		world = new World();
		int height = 1080;
		int width = 1080;
		//loadScene("scene/Mig-31 Foxhound.obj",world);
		//create_scene(world);
		loadScene("scene/sample.obj", world);
		System.out.println("Loaded scene");
		System.out.flush();

		putTuple(new WorldTuple(world));

		masterSchedule(leapfrog);
		String workerArgs[] = {Integer.toString(height), Integer.toString(width)};
		masterFor(0, height-1, RayTraceWorker.class).args(workerArgs);

		String outPutTaskArg[] = {Integer.toString(height), Integer.toString(width),
				"../output/renderedImage.png"};

		rule().task(ImgMaker.class).args(outPutTaskArg).runInJobProcess();

	}

}

/**
 * After the world is loaded in job main process the world is sent to every
 * worker. through this tuple
 */
class WorldTuple extends Tuple {
	World world;

	public WorldTuple() {}

	public WorldTuple(World world) {
		this.world = world;
	}

	public World getWorld() {
		return this.world;
	}

	public void writeOut(OutStream out) throws IOException {
		out.writeObjectArray(world.worldObjects);
		out.writeObjectArray(world.triangleLights);
	}

	public void readIn(InStream in) throws IOException {
		world.worldObjects = (WorldObject[])in.readObjectArray();
		world.triangleLights = (WorldObject[])in.readObjectArray();
	}
}

/**
 * Class type used to write out pixel data from ray trace compute
 */
class ImgTuple extends Tuple {
	public int row;               // Row index
	public ColorArray pixelData;  // Row's pixel data

	public ImgTuple() { }

	public ImgTuple(int row, ColorArray pixelData) {
			this.row = row;
			this.pixelData = pixelData;
	}

	public void writeOut(OutStream out) throws IOException {
			out.writeUnsignedInt (row);
			out.writeObject (pixelData);
	}

	public void readIn(InStream in) throws IOException {
			row = in.readUnsignedInt();
			pixelData = (ColorArray) in.readObject();
	}
}

class ImgMaker extends Task {

	int height;
	int width;
	File outFile;

	ColorPngWriter writer;
	ColorImageQueue imageQueue;

	public void main(String args[]) throws Exception {
		height = Integer.parseInt(args[0]);
		width = Integer.parseInt(args[1]);
		outFile = new File(args[2]);

		writer = new ColorPngWriter (height, width, new BufferedOutputStream(
				new FileOutputStream(outFile)));

		outFile.setReadable(true, false);
		outFile.setWritable(true, false);
		imageQueue = writer.getImageQueue();


		parallelDo(
			new Section() {
				public void run() throws Exception {
					ImgTuple template = new ImgTuple();
					ImgTuple tuple;

					for (int i = 0; i < height; i++) {
						tuple = (ImgTuple)takeTuple(template);
						imageQueue.put(tuple.row, tuple.pixelData);
					}

				}
			},

			new Section() {
				public void run() throws Exception {
					writer.write();
				}
			}
		);

	}

}

class RayTraceWorker extends Task{
	Vector eyePoint;
	Vector lookAt;
	Vector up;
	int focalLength;
	double projectionZ;

	// For writing PNG image file.
	ColorPngWriter writer;
	ColorImageQueue imageQueue;
	File filename;

	public void setCamera(Vector eyePoint, Vector lookAt, Vector up, int focalLength) {
		this.eyePoint = eyePoint;
		this.lookAt = lookAt;
		this.up = up;
		this.focalLength = focalLength;
		this.filename = new File("../output/renderedImage.png");
	}

	private void render(final World world,final int width,final int height)
			throws Exception {

		Vector n = eyePoint.subtract(lookAt);

		// Normalize vectors
		n.normalize();
		projectionZ = n.multiply(-focalLength).z;
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
		filename.setReadable(true, false);
		filename.setWritable(true, false);
		imageQueue = writer.getImageQueue();
		

		// Init projection plane
		final Vector imageOrigin = new Vector(-width/2.0, -height/2.0,
				projectionZ);

		final Random sampler = new Random(42);

		//KDTreeWO objects = new KDTreeWO(world.worldObjects);

		// Iterate over all pixels, and compute radiance
		// TODO: Multisampling / width height given in world coordinates
		workerFor(). schedule(leapfrog).exec (new Loop() {
			ColorArray pixelRow;
			Ray ray;
			Vector pixelPosition;

			public void start() {
				pixelRow = new ColorArray (width);
				ray = new Ray();
				pixelPosition = new Vector(0, 0, projectionZ);
			}
			public void run (int row) throws Exception {
							
				//skip even numbers saves ALOT of computation!!
				// if (oddNumber % 2 == 0) {
				// 	return;
				// }
				// retrieve original odd number from n of 2n + 1 form
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
				putTuple(new ImgTuple(row, pixelRow));
			}//run method ends

		});//workerFor ends
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



	public void main(String[] args) throws Exception {

		WorldTuple worldTupleTemplate = new WorldTuple();

		WorldTuple worldTuple = readTuple(worldTupleTemplate);

		World world = worldTuple.getWorld();

		eyePoint = new Vector(75, 60, 540);
		lookAt = new Vector(75, 60, 530);
		up = new Vector(0, 1, 0);
		focalLength = 1080;

		int height = Integer.parseInt(args[0]);
		int width = Integer.parseInt(args[1]);
		
		//setCameraSeq(eyePoint, lookAt, up, focalLength);

		render(world, width, height);
	}


}