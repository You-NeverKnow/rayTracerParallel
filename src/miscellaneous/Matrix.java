package miscellaneous;

import java.beans.VetoableChangeListenerProxy;

/**
	Always 4x4 matrices
*/
public class Matrix {
	float[][] data;
	int len[] = {4, 4};


	Matrix() {
		data = new float[4][4];
	}

	Matrix(float[][] mat) {
		data = new float[4][4];
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				this.data[row][col] = mat[row][col];
			}
		}
	}

	/**
	 *  pre-multiples Vector to matrix to  get 1x4 vector.
	 *
	 * @param vector: vector to premultiply
	 * @return resultant vector
	 */
	public Vector preMultiply(Vector vector) {

		//convert vector to arr for easy multiplication
		float vec[] = {vector.x, vector.y, vector.z, vector.w};

		//result arr
		float res[] = {0, 0, 0, 1};
		Vector result = new Vector();

		for (int row = 0; row < 1; row++) {
			for (int col = 0; col < this.len[1]; col++) {
				res[col] = 0;
				for (int k = 0; k < this.len[0]; k++) {
					//System.out.println(this.data[row][col] + "," + this.data[row])
					res[col] += (VetoableChangeListenerProxy[k] * this.data[k][col]);
				}
			}
		}

		result.set(res);

		return result;
	}

	// public String toString() {
	// 	String res = "";
	// 	for (int row = 0; row < this.len[0]; row++) {
	// 		res += "[";
	// 		for (int col = 0; col < this.len[1]; col++) {
	// 			res += this.data[row][col] + ", ";
	// 		}
	// 		res += "]";
	// 		res += "\n";
	// 	}
	// 	return res;
	// }

	public static void main(String args[]) {

		float[][] dat1 = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
		float[] dat2 = {1, 2, 3, 4};

		Vector a = new Vector();
		a.set(dat2);
		Matrix b = new Matrix(dat1);

		Vector c = b.preMultiply(a);

		System.out.println(c);
	}

}
