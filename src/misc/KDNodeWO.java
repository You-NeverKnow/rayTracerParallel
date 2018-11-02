package misc;

import World.WorldObject;
import java.util.ArrayList;
import java.util.Random;

public class KDNode {

    //Stores the current node's bounding box
    BoundingBox boundingBox;

    //left and right children
    KDNode left, right;

    //world objects within this KD node.
    WorldObject objects[];

    int split_axis;
    double split_val;


    /**
     * Build KD node with just a list of world objects. Meant to be invoked only
     * for root KD node.
     * @param objects all world objects in the scene.
     */
    public KDNode(WorldObject objects[]) {

        this.objects = new WorldObject[objects.length];

        for ( int idx = 0; idx < objects.length ; idx++) {
            this.objects[idx] = objects[idx];
        }

        findBoundingBox(objects);

        left = null;
        right = null;


    }

    /**
     * Build KD node based on bounding box. Invoked for all child nodes.
     * @param boundingBox Bounding box object that bounds this node. primitives sorted
     *                    based on this box.
     */
    private KDNode(BoundingBox boundingBox) {

        this.boundingBox = boundingBox;

        left = null;
        right = null;
    }

    /**
     * Finds bounding box given an array of primitives. Inefficient but is meant
     * to be called only once for root KD node.
     *
     * @param objects Array of all world objects in scene
     */
    private void findBoundingBox(WorldObject objects[]) {

        //sort on x-axis to get biggest and smallestx
        mergesort(objects, 0, objects.length, 'x');
        double minX = objects[0].getBoundingBox().getMinX();
        double maxX = objects[objects.length - 1].getBoundingBox().getMaxX();

        mergesort(objects, 0, objects.length, 'y');
        double minY = objects[0].getBoundingBox().getMinY();
        double maxY = objects[objects.length - 1].getBoundingBox().getMaxY();

        mergesort(objects, 0, objects.length, 'z');
        double minZ = objects[0].getBoundingBox().getMinZ();
        double maxZ = objects[objects.length - 1].getBoundingBox().getMaxZ();


        this.boundingBox = new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);

    }

    /**
     * Add world objects from the array of world objects based on the specified indices
     * @param objects Array of all world objects
     */
    private void addPrimitives(WorldObject objects[]) {


        for (int idx = 0; idx < objects.length; idx ++) {
            this.objects[idx] = objects[idx];
        }
    }

    /**
     *
     * Checks if the primitive is inside the KDNode based on its bounding box
     * @param primitive primitive which is to be checked.
     * @return
     */
    private boolean isInside(WorldObject primitive) {

        BoundingBox boundingBox = primitive.getBoundingBox();

        //extremes of parameter BB
        double minX, minY, minZ, maxX, maxY, maxZ;

        //extremes of this BB
        double thisMinX, thisMinY, thisMinZ, thisMaxX, thisMaxY, thisMaxZ;

        minX = boundingBox.getMinX();
        minY = boundingBox.getMinY();
        minZ = boundingBox.getMinZ();

        maxX = boundingBox.getMaxX();
        maxY = boundingBox.getMaxY();
        maxZ = boundingBox.getMaxZ();

        thisMinX = this.boundingBox.getMinX();
        thisMinY = this.boundingBox.getMinY();
        thisMinZ = this.boundingBox.getMinZ();

        thisMaxX = this.boundingBox.getMaxX();
        thisMaxY = this.boundingBox.getMaxY();
        thisMaxZ = this.boundingBox.getMaxZ();


        // if just the walls of bounding box and KDnode intersect
        // the primitive is still considered to be outside the
        if (minX >= thisMaxX || maxX <= thisMinX) {
            return false;
        } else if (minY >= thisMaxY || maxY <= thisMinY) {
            return false;
        } else if (minZ >= thisMaxZ || maxZ <= thisMinZ) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Splits current node into smaller child nodes and so on until child node has single child
     * Uses a helper function for abstraction.
     * @param root of the KD tree
     */
    public static void buildTree(KDNode root) {
        root.__buildTree(0);
    }

    public void __buildTree(int depth) {

        if ( this.objects.length <= 1 ) {
            return;
        }


        split_axis = depth % 3;

        //split value found on naive criteria
        //x-axis
        if ( split_axis == 0 ) {
            split_val = ( boundingBox.getMaxX() + boundingBox.getMinX() ) / 2;

        //y-axis
        } else if ( split_axis == 1) {
            split_val = ( boundingBox.getMaxY() + boundingBox.getMinY() ) / 2;

        //z-axis
        } else {
            split_val = ( boundingBox.getMaxZ() + boundingBox.getMinZ() ) / 2;
        }

        BoundingBox child[] = splitNode(split_axis, split_val);

        left = new KDNode(child[0]);

        right = new KDNode((child[1]));

        ArrayList<WorldObject> leftPrimitives = new ArrayList<>(), rightPrimitives = new ArrayList<>();


        for ( int idx = 0; idx < objects.length; idx++) {

            //objects intersecting split plane would be part
            //of both children
            if (this.left.isInside(objects[idx])) {
                leftPrimitives.add(objects[idx]);
            }
            if ( this.right.isInside(objects[idx])) {
                rightPrimitives.add(objects[idx]);
            }
        }

        left.addPrimitives((WorldObject[]) leftPrimitives.toArray());

        right.addPrimitives((WorldObject[]) rightPrimitives.toArray());

        left.__buildTree(depth+1);

        right.__buildTree(depth+1);


    }

    public IntersectionData traverse(Ray ray) {
        if (objects.length == 1) {
            return objects[0].intersect(ray);
        }


        IntersectionData interDat1, interDat2;
        Vector rayIntersects[] = boxIntersection(ray);

        double entry, exit;



        if (split_axis == 0) {
            entry = rayIntersects[0].x;
            exit = rayIntersects[1].x;
        } else if(split_axis == 1) {
            entry = rayIntersects[0].y;
            exit = rayIntersects[1].y;
        } else {
            entry = rayIntersects[0].z;
            exit = rayIntersects[1].z;
        }

        //traversal
        if ( entry <= split_val) {
            if ( exit < split_val) {
                return left.traverse(ray);
            } else {
                if (exit == split_val) {
                    Random rand = new Random();

                    if ( rand.nextDouble() > 0.5 ) {
                        return right.traverse(ray);
                    } else {
                        return left.traverse(ray);
                    }

                } else {
                    interDat1 = left.traverse(ray);
                    interDat2 = right.traverse(ray);

                    if ( interDat1.distance < interDat2.distance ) {
                        return interDat1;
                    } else {
                        return interDat2;
                    }
                }
            }
        } else {
            if(exit > split_val) {
                return right.traverse(ray);
            } else {
                interDat1 = left.traverse(ray);
                interDat2 = right.traverse(ray);

                if ( interDat1.distance < interDat2.distance ) {
                    return interDat1;
                } else {
                    return interDat2;
                }
            }
        }


    }

    /**
     * Computes entry and exit points of bounding box of KD node
     *
     * TODO: HANDLE intersections at corners, cz might be erroneous
     *
     * @param ray ray for which intersection has to be checked.
     * @return Array of 2 Vectors {entry_vec, exit_vec}
     */
    private Vector[] boxIntersection(Ray ray) {

        // entry at 0 and exit at 1
        Vector res[] = new Vector[2];

        // get the six planes of bounding box
        double minX = this.boundingBox.getMinX();
        double minXDist;
        double maxX = this.boundingBox.getMaxX();
        double maxXDist;
        double minY = this.boundingBox.getMinY();
        double minYDist;
        double maxY = this.boundingBox.getMaxY();
        double maxYDist;
        double minZ = this.boundingBox.getMinZ();
        double minZDist;
        double maxZ = this.boundingBox.getMaxZ();
        double maxZDist;

        double param;
        Vector intersectPtMinX = new Vector();
        Vector intersectPtMaxX = new Vector();
        Vector intersectPtMinY = new Vector();
        Vector intersectPtMaxY = new Vector();
        Vector intersectPtMinZ = new Vector();
        Vector intersectPtMaxZ = new Vector();



        if ( ray.direction.x == 0 ) {

            //ray parallel to plane perpendicular to x
            minXDist = Double.POSITIVE_INFINITY;
            maxXDist = Double.POSITIVE_INFINITY;


        } else {

            // ray may pass through one of the walls

            param = (minX - ray.origin.x) / ray.direction.x;

            // origin + direction * param;
            intersectPtMinX = ray.origin.add(ray.direction.multiply(param));
            minXDist = intersectPtMinX .subtract(ray.origin).norm();

            //intersection point not on actual box
            if (!boundingBox.isPointOnBox(intersectPtMinX)) {
                minXDist = Double.POSITIVE_INFINITY;
            }

            param = (maxX - ray.origin.x) / ray.direction.x;

            // origin + direction * param;
            intersectPtMaxX = ray.origin.add(ray.direction.multiply(param));
            maxXDist = intersectPtMaxX.subtract(ray.origin).norm();

            //intersection point not on actual box
            if (!boundingBox.isPointOnBox(intersectPtMaxX)) {
                maxXDist = Double.POSITIVE_INFINITY;
            }
        }


        if ( ray.direction.y == 0 ) {

            //ray parallel to plane perpendicular to x
            maxYDist = Double.POSITIVE_INFINITY;
            minYDist = Double.POSITIVE_INFINITY;
        } else {
            //min Y

            // origin.y + param * direction.y = miny
            param = (minY - ray.origin.y) / ray.direction.y;

            // origin + direction * param;
            intersectPtMinY = ray.origin.add(ray.direction.multiply(param));
            minYDist = intersectPtMinY.subtract(ray.origin).norm();

            //intersection point not on actual box
            if (!boundingBox.isPointOnBox(intersectPtMinY)) {
                minYDist = Double.POSITIVE_INFINITY;
            }

            //max Y


            param = (maxY - ray.origin.y) / ray.direction.y;

            // origin + direction * param;
            intersectPtMaxY = ray.origin.add(ray.direction.multiply(param));
            maxYDist = intersectPtMaxY.subtract(ray.origin).norm();
            if (!boundingBox.isPointOnBox(intersectPtMaxY)) {
                maxYDist = Double.POSITIVE_INFINITY;
            }

        }

        if ( ray.direction.z == 0 ) {

            //ray parallel to plane perpendicular to z

            maxZDist = Double.POSITIVE_INFINITY;
            minZDist = Double.POSITIVE_INFINITY;

        } else {
            //min Z


            param = (minZ - ray.origin.z) / ray.direction.z;

            // origin + direction * param;
            intersectPtMinZ = ray.origin.add(ray.direction.multiply(param));
            minZDist = intersectPtMinZ.subtract(ray.origin).norm();
            if (!boundingBox.isPointOnBox(intersectPtMinZ)) {
                minZDist = Double.POSITIVE_INFINITY;
            }


            //max Z

            // origin.y + param * direction.y = miny
            param = (maxZ - ray.origin.z) / ray.direction.z;

            // origin + direction * param;
            intersectPtMaxZ = ray.origin.add(ray.direction.multiply(param));
            maxZDist = intersectPtMaxZ.subtract(ray.origin).norm();
            if (!boundingBox.isPointOnBox(intersectPtMaxZ)) {
                maxZDist = Double.POSITIVE_INFINITY;
            }
        }

        //FINDING THE SMALLEST 2 DISTANCES as entry and exit points

        double dists[] = {minXDist, maxXDist, minYDist, maxYDist, minZDist, maxZDist};

        int smallest_idx = -1;
        int sec_smallest_idx = -1;
        //get smallest
        for (int idx = 0 ; idx < dists.length; idx++) {
            if (dists[idx] < Double.POSITIVE_INFINITY) {
                if (smallest_idx == -1) {
                    smallest_idx = idx;
                } else {
                    sec_smallest_idx = idx;
                }
            }
        }


        int temp_idx;
        if (dists[sec_smallest_idx] < dists[smallest_idx]) {
            temp_idx = smallest_idx;
            smallest_idx = sec_smallest_idx;
            sec_smallest_idx = temp_idx;
        }

        if (smallest_idx == 0) {
            res[0] = intersectPtMinX;
        } else if (smallest_idx == 1) {
            res[0] = intersectPtMaxX;
        } else if (smallest_idx == 2) {
            res[0] = intersectPtMinY;
        } else if (smallest_idx == 3) {
            res[0] = intersectPtMaxY;
        } else if (smallest_idx == 4) {
            res[0] = intersectPtMaxZ;
        } else if (smallest_idx == 5) {
            res[0] = intersectPtMinZ;
        }

        if (sec_smallest_idx == 0) {
            res[1] = intersectPtMinX;
        } else if (sec_smallest_idx == 1) {
            res[1] = intersectPtMaxX;
        } else if (sec_smallest_idx == 2) {
            res[1] = intersectPtMinY;
        } else if (sec_smallest_idx == 3) {
            res[1] = intersectPtMaxY;
        } else if (sec_smallest_idx == 4) {
            res[1] = intersectPtMaxZ;
        } else if (sec_smallest_idx == 5) {
            res[1] = intersectPtMinZ;
        }

        return res;

    }


    /**
     * Split bounding box into 2 bounding boxes
     * @param axis axis perpendicular to which to split the bounding box
     *             0 : x-axis
     *             1 : y-axis
     *             2 : z-axis
     *
     * @param value point on axis at which to split
     *
     * In other words axis: x and value:5 would split at x=5 plane
     *
     * @return objects of split bounding boxes
     */
    private BoundingBox[] splitNode(int axis, double value) {
        this.split_axis = axis;
        this.split_val = value;

        BoundingBox boxes[] = new BoundingBox[2];

        //x-axis
        if (axis == 0) {

            //for left split box
            double minX = boundingBox.getMinX();
            double minY = boundingBox.getMinY();
            double minZ = boundingBox.getMinZ();

            double maxX = value;
            double maxY = boundingBox.getMaxY();
            double maxZ = boundingBox.getMaxZ();

            boxes[0] = new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);

            //for right split box
            minX = value;
            maxX = boundingBox.getMaxX();

            boxes[1] = new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);

            //y-axis
        } else if (axis == 1) {

            //for left split box
            double minX = boundingBox.getMinX();
            double minY = boundingBox.getMinY();
            double minZ = boundingBox.getMinZ();

            double maxX = boundingBox.getMaxX();
            double maxY = value;
            double maxZ = boundingBox.getMaxZ();

            boxes[0] = new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);

            //for right split box
            minY = value;
            maxY = boundingBox.getMaxY();

            boxes[1] = new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);

        //z-axis
        } else if (axis == 2) {

            //for left split box
            double minX = boundingBox.getMinX();
            double minY = boundingBox.getMinY();
            double minZ = boundingBox.getMinZ();

            double maxX = boundingBox.getMaxX();
            double maxY = boundingBox.getMaxZ();
            double maxZ = value;

            boxes[0] = new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);

            //for right split box
            minZ = value;
            maxZ = boundingBox.getMaxZ();

            boxes[1] = new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);
        }

        return boxes;
    }


    private void mergesort(WorldObject li[], int lo, int hi, char axis) {
        if ( (hi - lo) > 1){
            int mid = (hi + lo) / 2;
            mergesort(li, lo, mid, axis);
            mergesort(li, mid, hi, axis);
            merge(li, lo, mid, hi, axis);
        }


    }

    private void merge(WorldObject li[], int lo, int mid, int hi, char axis){
        int leftindex = lo;
        int rightindex = mid;
        WorldObject aux[] = new WorldObject[li.length];
        int aux_idx = 0;

        if (axis == 'x') {
            while (leftindex < mid && rightindex < hi) {
                if (li[leftindex].getFirstCorner().x <= li[rightindex].getFirstCorner().x) {
                    aux[aux_idx] = li[leftindex];
                    leftindex++;
                    aux_idx++;
                } else {
                    aux[aux_idx] = li[rightindex];
                    rightindex++;
                    aux_idx++;
                }
            }
        } else if (axis == 'y') {
            while (leftindex < mid && rightindex < hi) {
                if (li[leftindex].getFirstCorner().y <= li[rightindex].getFirstCorner().y) {
                    aux[aux_idx] = li[leftindex];
                    leftindex++;
                    aux_idx++;
                } else {
                    aux[aux_idx] = li[rightindex];
                    rightindex++;
                    aux_idx++;
                }
            }
        } else if (axis == 'z') {
            while (leftindex < mid && rightindex < hi) {
                if (li[leftindex].getFirstCorner().x <= li[rightindex].getFirstCorner().z) {
                    aux[aux_idx] = li[leftindex];
                    leftindex++;
                    aux_idx++;
                } else {
                    aux[aux_idx] = li[rightindex];
                    rightindex++;
                    aux_idx++;
                }
            }
        }

        while (leftindex < mid) {
            aux[aux_idx] = li[leftindex];
            leftindex++;
            aux_idx++;
        }

        while (rightindex < hi) {
            aux[aux_idx] = li[rightindex];
            rightindex++;
            aux_idx++;
        }

        for(int i = lo, idx = 0; i < hi; i++, idx++) {
            li[i] = aux[idx];
        }

    }
}
