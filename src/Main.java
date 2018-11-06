import World.World;
import World.Sphere;
import World.Triangle;
import World.WorldObject;
import misc.Vector;
import java.io.IOException;

/**
 * Main
 */
public class Main {

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

    public static void main(String[] args)
            throws IOException, InterruptedException {

        World world = new World();
        create_scene(world);

        Vector eyePoint = new Vector(75, 60, 540);
        Vector lookAt = new Vector(75, 60, 530);
        Vector up = new Vector(0, 1, 0);

        Camera camera = new Camera(eyePoint, lookAt, up,1080);
        camera.render(world, 1080, 1080);
    }
    

}

