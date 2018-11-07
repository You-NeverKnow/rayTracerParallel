package World;

import miscellaneous.IntersectionData;
import miscellaneous.Ray;
import miscellaneous.Vector;


/**
 * Triangle
 */
public class Triangle extends WorldObject {
    Vector vertex1;
    Vector vertex2;
    Vector vertex3;
    Vector normal;

    public Triangle(){};

    public Triangle(Vector vertex1, Vector vertex2, Vector vertex3) {
        this.vertex1 = new Vector(vertex1.x, vertex1.y, vertex1.z);
        this.vertex2 = new Vector(vertex2.x, vertex2.y, vertex2.z);
        this.vertex3 = new Vector(vertex3.x, vertex3.y, vertex3.z);

        normal = vertex2.subtract(vertex1).cross(vertex3.subtract(vertex1));
        normal.normalize();
    }

    @Override
    public void transform(double[][] transformMatrix) {
        // Transform all vertices and normal
        this.vertex1.transform(transformMatrix);
        this.vertex2.transform(transformMatrix);
        this.vertex3.transform(transformMatrix);

        normal = vertex2.subtract(vertex1).cross(vertex3.subtract(vertex1));
        normal.normalize();
    }

    @Override
    public IntersectionData intersect(Ray ray) {

        Vector e1 = this.vertex2.subtract(this.vertex1);
        Vector e2 = this.vertex3.subtract(this.vertex1);
        Vector p = ray.direction.cross(e2);

        double divisor = p.dot(e1);

        // Ray parallel to triangle
        if (divisor == 0)
        {
            return new IntersectionData();
        }

        Vector t = ray.origin.subtract(this.vertex1);
        Vector q = t.cross(e1);

        double distance = q.dot(e2) / divisor;
        double u = p.dot(t) / divisor;
        double v = q.dot(ray.direction) / divisor;

        // No intersection
        if ((u < 0) || (v < 0) || (u + v > 1)) {
            return new IntersectionData();
        }

        if (distance >= 0) {
            Vector intersectionPoint = ray.origin.add(
                                            ray.direction.multiply(distance));
//            System.out.println("Ray dir = " + ray.direction);

            return new IntersectionData(distance, intersectionPoint,
                                        ray.direction,
                                        this.normal, this.color, this);
        }

        return new IntersectionData();
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "vertex1=" + vertex1 +
                ", vertex2=" + vertex2 +
                ", vertex3=" + vertex3 +
                ", normal=" + normal +
                '}';
    }
}