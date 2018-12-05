import World.World;
import World.Sphere;
import World.Triangle;
import World.WorldObject;
import edu.rit.pj2.Task;
import miscellaneous.Phong;
import miscellaneous.Vector;
import java.io.IOException;

/**
 * Main
 */
public class RayTraceSeq extends Task {

    public void main(String[] args) throws IOException, InterruptedException {
        World world = new World();
        CreateScene.create_scene(world);

        Vector eyePoint = new Vector(75, 60, 540);
        Vector lookAt = new Vector(75, 60, 530);
        Vector up = new Vector(0, 1, 0);

        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);

        Camera camera = new Camera(eyePoint, lookAt, up,1080);
        camera.render(world, width, height);

    }
}

