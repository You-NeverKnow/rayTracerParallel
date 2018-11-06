package misc;

import World.WorldObject;

import java.util.ArrayList;

public class KDNodePt {

	Photon split_photon;
    int num_photons;

	KDNodePt left, right;

	int split_axis;
	double split_val;

	public KDNodePt() {

    }

	public KDNodePt(Photon p, int axis, double val) {
	    split_photon = p;
	    split_axis = axis;
	    split_val = val;
    }

    public static KDNodePt buildTree(Photon photons[]) {
	    KDNodePt root = new KDNodePt();
        root.__buildTree(0, photons);
        return root;
    }

    public void __buildTree(int depth, Photon photons[]) {

	    depth = depth % 3;

        split_axis = depth;
        split_photon = getMedian(photons, split_axis);
        Photon leftPhotons[] = new Photon[((int)photons.length / 2) + 1 ];
        Photon rightPhotons[] = new Photon[((int)photons.length / 2) + 1 ];
        int right_idx = 0, left_idx = 0;
        if (split_axis == 0) {
            for (int idx = 0; idx < photons.length; idx++) {
                if (photons[idx].position.x < split_val) {
                    leftPhotons[left_idx] = photons[idx];
                    left_idx++;
                } else if(photons[idx].position.x > split_val) {
                    rightPhotons[right_idx] = photons[idx];
                    right_idx++;
                } else {
                    num_photons++;
                }
            }
        } else if (split_axis == 1) {
            for (int idx = 0; idx < photons.length; idx++) {
                if (photons[idx].position.y < split_val) {
                    leftPhotons[left_idx] = photons[idx];
                    left_idx++;
                } else if (photons[idx].position.z > split_val) {
                    rightPhotons[right_idx] = photons[idx];
                    right_idx++;
                } else {
                    num_photons++;
                }
            }
        } else {
            for (int idx = 0; idx < photons.length; idx++) {
                if (photons[idx].position.z < split_val) {
                    leftPhotons[left_idx] = photons[idx];
                    left_idx++;
                } else if (photons[idx].position.z > split_val) {
                    rightPhotons[right_idx] = photons[idx];
                    right_idx++;
                } else {
                    num_photons++;
                }
            }
        }


        left = new KDNodePt();
        left.__buildTree(depth+1, leftPhotons);
        right = new KDNodePt();
        right.__buildTree(depth+1, rightPhotons);
    }



    private Photon getMedian(Photon li[], int axis) {
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

    private void mergesort(Photon li[], int lo, int hi, char axis) {
        if ( (hi - lo) > 1){
            int mid = (hi + lo) / 2;
            mergesort(li, lo, mid, axis);
            mergesort(li, mid, hi, axis);
            merge(li, lo, mid, hi, axis);
        }


    }

    private void merge(Photon li[], int lo, int mid, int hi, char axis){
        int leftindex = lo;
        int rightindex = mid;
        Photon aux[] = new Photon[li.length];
        int aux_idx = 0;

        if (axis == 'x') {
            while (leftindex < mid && rightindex < hi) {
                if (li[leftindex].position.x <= li[rightindex].position.x) {
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
                if (li[leftindex].position.y <= li[rightindex].position.y) {
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
                if (li[leftindex].position.x <= li[rightindex].position.z) {
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
