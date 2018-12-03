package misc;

public class KDTreePt {

    KDNodePt root;

    public KDTreePt(Vector photons[]) {
        root = KDNodePt.buildTree(photons);
    }

    public Vector[] findClosest(int num_near, Vector pt) {
        return root.findClosest(num_near, pt);
    }



}
