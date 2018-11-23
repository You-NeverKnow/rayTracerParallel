package misc;

/**
 * Class for a fixed length priority queue
 */
public class PriorityQ {

    Qelement q[];
    Vector center;

    // max distance from center
    double maxDist;

    //idx of photon with max distance in queue
    int farPtIdx;

    //number of elements in queue
    int size;

    /**
     * Create a priority queue
     * @param len fixed length of priority queue
     * @param center point with which to find distance compare and sort priority queue on.
     */
    public PriorityQ(int len, Vector center) {
        this.q = new Qelement[len+1];
        this.center = center;
        maxDist = Double.POSITIVE_INFINITY;
        size = 0;
    }

    /**
     * swim operation of heap performed after insert at low level
     * to get element to apt position.
     * @param idx idx of element on which to perform swim op
     */
    private void swim(int idx) {

        while ( idx > 1  && this.q[idx].distance < this.q[idx/2].distance ) {
            Qelement temp = this.q[idx];
            this.q[idx] = this.q[idx/2];
            this.q[idx/2] = temp;
        }

    }

    /**
     * Insert a vector in the
     * @param p position to insert in the kd-tree
     */
    public void insert(Vector p) {
        double distance = p.subtract(this.center).norm();

        //idx at which element was added
        int cursor;
        // queue is empty
        if ( size == 0 ) {
            cursor = 1;
            q[cursor] = new Qelement();
            q[cursor].p = p;
            q[cursor].distance = distance;
            maxDist = distance;
            farPtIdx = 1;
            size++;

        // queue is full
        } else if (size == q.length-1) {
            int idx = size;
            cursor = farPtIdx;
            q[farPtIdx].p = p;
            q[farPtIdx].distance = distance;
        } else {
            cursor = size + 1;
            q[cursor] = new Qelement();
            q[cursor].p = p;
            q[cursor].distance = distance;
            if (distance > maxDist) {
                farPtIdx = size + 1;
                maxDist = distance;
            }
            size++;
        }

        swim(cursor);


    }

    /**
     * Returns maximum distance of the point in queue. inf if queue is not filled completely yet.
     * @return maximum distance of all points in kd tree.
     */
    public double getMaxdist() {
        if (size < (q.length-1)) {
            return Double.POSITIVE_INFINITY;
        } else {
            return maxDist;
        }
    }

    public String toString() {
        String res = "points";

        for (int idx = 1; idx <= this.size; idx++) {
            System.out.println(idx);
            res += q[idx].p;// + 
            //", " + q[idx].distance;
        }

        return res;
    }

    /**
     * @return returns backing array of vectors in queue
     */
    public Vector[] toArray() {
        Vector res[] = new Vector[this.size];

        for (int idx = 0; idx < res.length; idx++) {
            res[idx] = this.q[idx+1].p;
        }

        return res;
    }

    public static void main(String args[]) {

        Vector a = new Vector(1,1,1);
        Vector b = new Vector(2,2,2);
        Vector c = new Vector(3,3,3);
        Vector d = new Vector(4,4,4);
        Vector e = new Vector(5,5,5);
        Vector center = new Vector(0,0,0);


        PriorityQ q = new PriorityQ(4, center);

        q.insert(a);
        q.insert(b);
        q.insert(e);
        System.out.println(q);
        q.insert(d);
        q.insert(c);
        System.out.println(q);

        Vector s[] = q.toArray();

        for (int idx = 0; idx < s.length; idx++) {
            System.out.println(s[idx]);
        }


    }

}


class Qelement {

    Vector p;
    double distance;

}