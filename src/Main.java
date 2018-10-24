import World.World;
import World.Triangle;
import World.WorldObject;
import miscellaneous.Vector;

import java.awt.*;
import java.io.IOException;

/**
 * Main
 */
public class Main {

    private static void create_scene(World world) {
        world.worldObjects = new WorldObject[2];

        int a = 100;
        Vector vertex0 = new Vector(a/2, 0, -10);
        Vector vertex1 = new Vector(a/2, 0, a/2);
        Vector vertex2 = new Vector(a, 0, -10);
        Vector vertex3 = new Vector(a, 0, a/2);

	    Triangle halfFloor1 = new Triangle(vertex0, vertex1, vertex2);
	    Triangle halfFloor2 = new Triangle(vertex1, vertex2, vertex3);
	    halfFloor1.color.rgb(0.99f, 0.0f, 0.0f);
	    halfFloor2.color.rgb(0.99f, 0.0f, 0.0f);

	    world.worldObjects[0] = halfFloor1;
	    world.worldObjects[1] = halfFloor2;
    }
        
    
    public static void main(String[] args)
            throws IOException, InterruptedException {

        World world = new World();
        create_scene(world);

        // DEBUG
//        for (WorldObject worldObject: world.worldObjects) {
//            System.out.println(worldObject);
//        }


        Vector eyePoint = new Vector(75, 60, 0);
        Vector lookAt = new Vector(75, 60, 10);
        Vector up = new Vector(0, 1, 0);

        Camera camera = new Camera(eyePoint, lookAt, up,20);
        camera.render(world, 1200, 800);
    }
    

}

