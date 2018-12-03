package misc;

public class KDNodePt {

	Vector split_photon;
    int num_photons;

	KDNodePt left, right;

	int split_axis;
	double split_val;

	public KDNodePt() {

    }

	public KDNodePt(Vector p, int axis, double val) {
	    split_photon = p;
	    split_axis = axis;
	    split_val = val;
	    num_photons = 0;
	    left = null;
	    right = null;
    }

    public static KDNodePt buildTree(Vector photons[]) {
	    KDNodePt root = new KDNodePt();
        root.__buildTree(0, photons);
        return root;
    }

    public void __buildTree(int depth, Vector photons[]) {

	    depth = depth % 3;

        split_axis = depth;
        split_photon = getMedian(photons, split_axis);
        Vector leftPhotons[] = new Vector[((int)photons.length / 2) + 1 ];
        Vector rightPhotons[] = new Vector[((int)photons.length / 2) + 1 ];
        int right_idx = 0, left_idx = 0;
        if (split_axis == 0) {
            for (int idx = 0; idx < photons.length; idx++) {
                if (photons[idx].x <= split_val) {
                    leftPhotons[left_idx] = photons[idx];
                    left_idx++;
                } else if(photons[idx].x > split_val) {
                    rightPhotons[right_idx] = photons[idx];
                    right_idx++;
                } else {
                    num_photons++;
                }
            }
        } else if (split_axis == 1) {
            for (int idx = 0; idx < photons.length; idx++) {
                if (photons[idx].y <= split_val) {
                    leftPhotons[left_idx] = photons[idx];
                    left_idx++;
                } else if (photons[idx].z > split_val) {
                    rightPhotons[right_idx] = photons[idx];
                    right_idx++;
                } else {
                    num_photons++;
                }
            }
        } else {
            for (int idx = 0; idx < photons.length; idx++) {
                if (photons[idx].z <= split_val) {
                    leftPhotons[left_idx] = photons[idx];
                    left_idx++;
                } else if (photons[idx].z > split_val) {
                    rightPhotons[right_idx] = photons[idx];
                    right_idx++;
                } else {
                    num_photons++;
                }
            }
        }


        // Create children only if there are photons left
        if (left_idx > 0) {
            left = new KDNodePt();
            left.__buildTree(depth + 1, leftPhotons);
        }

        if (right_idx > 0) {
            right = new KDNodePt();
            right.__buildTree(depth + 1, rightPhotons);
        }

    }


    public Vector[] findClosest(int num_near, Vector center) {
	    PriorityQ pq = new PriorityQ(num_near, center);

	    this.traverse(pq);

        return pq.toArray();
    }

    /**
     * Traverse kd tree
     * @param pq priority queue which contains information relevant for traversals
     *           and also stores nearest points
     */
    private void traverse(PriorityQ pq) {

	    for(int idx = 0; idx < this.num_photons; idx++) {
	        pq.insert(split_photon);
        }

	    if( pq.center.x <= split_val ) {
	        if (left != null && isfeasible(pq, split_axis, split_val, 'l')) {
	            left.traverse(pq);
	        }

	        if (right != null && isfeasible(pq, split_axis, split_val, 'l')) {
	            right.traverse(pq);
	        }

	    } else {
	        if (right != null && isfeasible(pq, split_axis, split_val, 'r')) {
	            right.traverse(pq);
	        }

	        if (left != null && isfeasible(pq, split_axis, split_val, 'r')) {
	            left.traverse(pq);
	        }
	    }

    }

    /**
     * Checks if traversing a sub tree could yield a closer point. Helps prune sub-trees for fast
     * compute.
     *
     * @param pq priority queue objects has max distance from center point as well as center point
     * @param split_axis axis along which node was split
     * @param split_val value on the axis at which node was split
     * @param direction left or right sub tree to be chekced
     *
     * @return Returns true if bounding box under consideration could have a point closer than
     *         the farthest point from center encountered till now.
     */
    private boolean isfeasible(PriorityQ pq, int split_axis, double split_val, char direction) {
        Vector center = pq.center;
        double maxDist = pq.maxDist;

        //left sub tree pruning
        if (direction == 'l') {
            if (split_axis == 0) {
                // center in same box
                if (pq.center.x <= split_val) {
                    return true;

                // center in opp box
                } else {
                    return Math.abs(pq.center.x - split_val) <= maxDist;
                }
            }else if (split_axis == 1) {
                // center in same box
                if (pq.center.y <= split_val) {
                    return true;

                    // center in opp box
                } else {
                    return Math.abs(pq.center.y - split_val) <= maxDist;
                }
            }else if (split_axis == 2) {
                // center in same box
                if (pq.center.z <= split_val) {
                    return true;

                    // center in opp box
                } else {
                    return Math.abs(pq.center.z - split_val) <= maxDist;
                }
            } else {
                throw new RuntimeException("Invalid split axis!!!!!");
            }

        // right sub tree pruning
        } else {
            if (split_axis == 0) {
                // center in same box
                if (pq.center.x > split_val) {
                    return true;

                    // center in opp box
                } else {
                    return Math.abs(pq.center.x - split_val) <= maxDist;
                }
            }else if (split_axis == 1) {
                // center in same box
                if (pq.center.y > split_val) {
                    return true;

                    // center in opp box
                } else {
                    return Math.abs(pq.center.y - split_val) <= maxDist;
                }
            }else if (split_axis == 2) {
                // center in same box
                if (pq.center.z > split_val) {
                    return true;

                    // center in opp box
                } else {
                    return Math.abs(pq.center.z - split_val) <= maxDist;
                }
            } else {
                throw new RuntimeException("Invalid split axis!!!!!");
            }
        }

    }


    private Vector getMedian(Vector li[], int axis) {
	    if (axis == 0) {
	        mergesort(li, 0, li.length, 'x');
        } else if (axis == 1) {
	        mergesort(li, 0, li.length, 'y');
        } else {
	        mergesort(li, 0, li.length, 'z');
        }

        int median_idx =  (li.length + 0) / 2;

        return li[median_idx];

    }

    private void mergesort(Vector li[], int lo, int hi, char axis) {
        if ( (hi - lo) > 1){
            int mid = (hi + lo) / 2;
            mergesort(li, lo, mid, axis);
            mergesort(li, mid, hi, axis);
            merge(li, lo, mid, hi, axis);
        }


    }

    private void merge(Vector li[], int lo, int mid, int hi, char axis){
        int leftindex = lo;
        int rightindex = mid;
        Vector aux[] = new Vector[li.length];
        int aux_idx = 0;

        if (axis == 'x') {
            while (leftindex < mid && rightindex < hi) {
                if (li[leftindex].x <= li[rightindex].x) {
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
                if (li[leftindex].y <= li[rightindex].y) {
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
                if (li[leftindex].x <= li[rightindex].z) {
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
