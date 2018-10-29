package World;

import miscellaneous.IntersectionData;
import miscellaneous.Ray;
import miscellaneous.Vector;

public class Sphere extends WorldObject {

    public double radius;
    public Vector center;

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
}
