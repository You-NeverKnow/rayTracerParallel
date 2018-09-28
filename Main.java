/**
 * Main
 */
public class Main {

    private WorldObject[] create_scene()
    {
    }
        
    
    public static void main(String[] args) {
        
        WorldObject[] worldObjects = create_scene(world_objects);
    
        Vector camera_position = new Vector(75, 60, -20);
        Vector camera_look_at = new Vector(75, 60, 0);
    
        Camera camera_one = new Camera(camera_position, camera_look_at, 20);
        camera_one.render(world_objects);
    }
    

}

