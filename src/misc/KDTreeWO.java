package misc;

import World.WorldObject;

/**
 *  KD tree class meant to store only world object as primitives.
 *  (Triangles and spheres)
 */
public class KDTreeWO {

    KDNodeWO root;

    /**
     * Creates a KD tree using an array of world objects
     * @param objects
     */
    public KDTreeWO(WorldObject objects[]) {

        root = new KDNodeWO(objects);

        KDNodeWO.buildTree(root);

    }

    /**
     * Traverses KD tree to find intersections if any with primitives.
     * @param ray Ray with which to find intersections of world objects.
     * @return Intersection Data of closest Intersection point to ray origin.
     */
    public IntersectionData getIntersection(Ray ray) {
        return root.traverse(ray);
    }

}
