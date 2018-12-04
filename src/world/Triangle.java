package world;

import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.io.Streamable;
import misc.BoundingBox;
import misc.IntersectionData;
import misc.Ray;
import misc.Vector;
import edu.rit.image.Color;

import java.io.IOException;


/**
 * Triangle
 */
public class Triangle extends WorldObject {
    Vector vertex1;
    Vector vertex2;
    Vector vertex3;
    Vector normal;

    // Stores bounding box which is a set of 8 vectors
    // Vector of bottom 4 corners first and vectors of
    // top 4 corners later.
    BoundingBox boundingBox;


    public Triangle(){};

    public Triangle(Vector vertex1, Vector vertex2, Vector vertex3) {
        this.vertex1 = new Vector(vertex1.x, vertex1.y, vertex1.z);
        this.vertex2 = new Vector(vertex2.x, vertex2.y, vertex2.z);
        this.vertex3 = new Vector(vertex3.x, vertex3.y, vertex3.z);

        normal = vertex2.subtract(vertex1).cross(vertex3.subtract(vertex1));
        normal.normalize();

        this.setBoundingBox();
    }

    public Triangle(Vector vertex1, Vector vertex2, Vector vertex3, Color color) {
        this.vertex1 = new Vector(vertex1.x, vertex1.y, vertex1.z);
        this.vertex2 = new Vector(vertex2.x, vertex2.y, vertex2.z);
        this.vertex3 = new Vector(vertex3.x, vertex3.y, vertex3.z);
        this.color = new Color(color);
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


        // Hit
//        distance = Math.abs(distance);

        if (distance >= 0) {
            Vector intersectionPoint = ray.origin.add(
                                            ray.direction.multiply(distance));

            return new IntersectionData(distance, intersectionPoint,
                                        this.normal, this.color, this);

        }

        return new IntersectionData();
    }

    private double min(double arr[]) {
        double m = arr[0];

        for ( int idx = 1; idx < arr.length; idx++ ) {
            if ( arr[idx] < m ) {
                m = arr[idx];
            }
        }

        return m;

    }

    private double max(double arr[]) {
        double m = arr[0];

        for ( int idx = 1; idx < arr.length; idx++ ) {
            if ( arr[idx] > m ) {
                m = arr[idx];
            }
        }

        return m;

    }

    @Override
    /**
     * Sets Axially aligned bounding box of the triangle
     * object for KD tree.
     */
    public void setBoundingBox() {

        double x[] = {vertex1.x, vertex2.x, vertex3.x};
        double y[] = {vertex1.y, vertex2.y, vertex3.y};
        double z[] = {vertex1.z, vertex2.z, vertex3.z};
        
        double minX = min(x);
        double minY = min(y);
        double minZ = min(z);

        double maxX = max(x);
        double maxY = max(y);
        double maxZ = max(z);

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

//    @Override
//    public void writeOut(OutStream out) throws IOException {
//        out.writeObject(vertex1);
//        out.writeObject(vertex2);
//        out.writeObject(vertex3);
//    }
//
//    @Override
//    public void readIn(InStream inStream) throws IOException {
//        vertex1 = (Vector)inStream.readObject();
//        vertex2 = (Vector)inStream.readObject();
//        vertex3 = (Vector)inStream.readObject();
//    }

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