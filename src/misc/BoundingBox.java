package misc;

import World.WorldObject;

import java.util.ArrayList;

public class BoundingBox {

    Vector corners[];

    double minX, maxX;
    double minY, maxY;
    double minZ, maxZ;

    /**
     * Build bounding box based on position of corners given as vectors
     *
     * @param corners Array of Vector specifying positions of corners.
     */
//    public BoundingBox(Vector corners[]) {
//
//        this.corners = new Vector[corners.length];
//
//        for (int idx = 0; idx < corners.length; idx++ ) {
//            this.corners[idx] = corners[idx];
//        }
//
//
//
//    }

    /**
     *  Build bounding box based on bounding coordinates of primitives
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @param minZ
     * @param maxZ
     */
    public BoundingBox(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {

        this.corners = new Vector[] {

                //bottom 4 corners (clockwise)
                new Vector(minX, minY, minZ),
                new Vector(maxX, minY, minZ),
                new Vector(maxX, minY, maxZ),
                new Vector(minX, minY, maxZ),

                //top 4 corners (clockwise)
                new Vector(minX, maxY, minZ),
                new Vector(maxX, maxY, minZ),
                new Vector(maxX, maxY, maxZ),
                new Vector(minX, maxY, maxZ)
        };

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;

    }

    public void buildBox() {
        this.corners = new Vector[] {

                //bottom 4 corners (clockwise)
                new Vector(minX, minY, minZ),
                new Vector(maxX, minY, minZ),
                new Vector(maxX, minY, maxZ),
                new Vector(minX, minY, maxZ),

                //top 4 corners (clockwise)
                new Vector(minX, maxY, minZ),
                new Vector(maxX, maxY, minZ),
                new Vector(maxX, maxY, maxZ),
                new Vector(minX, maxY, maxZ)
        };
    }

    /**
     *
     * Checks if the position/point is on the KDNodeWO bounding box
     * @param point position which is to be checked.
     * @return true if point is on the box
     */
    public boolean isPointOnBox(Vector point) {
        // get the six planes of bounding box
        double minX = this.getMinX();
        double minXDist;
        double maxX = this.getMaxX();
        double maxXDist;
        double minY = this.getMinY();
        double minYDist;
        double maxY = this.getMaxY();
        double mscYDist;
        double minZ = this.getMinZ();
        double minDist;
        double maxZ = this.getMaxZ();
        double maxZDist;


        // if just the walls of bounding box and KDnode intersect
        // the primitive is still considered to be outside the
        if (minX > point.x || maxX < point.x) {
            return false;
        } else if (minY > point.y || maxY <= point.y) {
            return false;
        } else if (minZ >=  point.z || maxZ <= point.z) {
            return false;
        } else {
            return true;
        }

    }


    public Vector getFirstCorner() {
        return corners[0];
    }


    public double getMinX() {

        //bottom left back corner
        return minX;
    }

    public double getMaxX() {

        //top right front corner
        return maxX;
    }

    public double getMinY() {

        //bottom left back corner
        return minY;

    }

    public double getMaxY() {

        //top right front corner
        return maxY;
    }

    public double getMinZ() {

        //bottom left back corner
        return minZ;
    }

    public double getMaxZ() {

        //top right front corner
        return maxZ;
    }
}
