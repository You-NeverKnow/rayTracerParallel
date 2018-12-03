package world;

import misc.BoundingBox;
import misc.IntersectionData;
import misc.Ray;
import misc.Vector;

public class Sphere extends WorldObject {

    public double radius;
    public Vector center;

    BoundingBox boundingBox;

    public Sphere(double radius, Vector center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void transform(double[][] transformMatrix) {
        this.center.transform(transformMatrix);
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "radius=" + radius +
                ", center=" + center +
                '}';
    }

    @Override
    public IntersectionData intersect(Ray ray) {

        double B = 2 * (
            ray.direction.x * (ray.origin.x - center.x) +
            ray.direction.y * (ray.origin.y - center.y) +
            ray.direction.z * (ray.origin.z - center.z)
            );

        double C =
                (ray.origin.x - center.x) * (ray.origin.x - center.x) +
                (ray.origin.y - center.y) * (ray.origin.y - center.y) +
                (ray.origin.z - center.z) * (ray.origin.z - center.z) -
                (radius * radius);

        // Real discriminant for intersection
        double discriminant = B * B - 4 * C;

        // No intersection
        if (discriminant < 0) {
            return new IntersectionData();
        }

        // Try smaller distance intersection point first
        double distance = (-B - Math.sqrt(discriminant))/2;
        if (distance >= 0) {
            Vector distanceAdder = ray.direction.multiply(distance);
            Vector intersectionPoint = ray.origin.add(distanceAdder);

            Vector normal = intersectionPoint.subtract(this.center);
            normal.normalize();
            return new IntersectionData(distance, intersectionPoint,
                                        normal, this.color, this);
        }

//        System.out.println("Sphere /? " + distance);
        return new IntersectionData();
    }

    @Override
    /**
     * Sets Axially aligned bounding box of the sphere
     * object for KD tree.
     */
    public void setBoundingBox() {

        double minX = center.x - radius;
        double minY = center.y - radius;
        double minZ = center.z - radius;

        double maxX = center.x + radius;
        double maxY = center.y + radius;
        double maxZ = center.z + radius;

        boundingBox = new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);
    }

    @Override
    /**
     *  Returns bounding box of this triangle
     *
     * @return bounding box as an array of Vector of length 8
     */
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    /**
     * Returns first corner stores in bounding box.
     *
     * @return  Vector object of first corner of bounding box.
     */
    public Vector getFirstCorner() {
        return boundingBox.getFirstCorner();
    }

}
