package miscellaneous;
/**
	Always 4x4 matrices
*/
public class Matrix {
	int[][] data;
	int len[] = {4, 4};


	Matrix() {
		data = new int[4][4];
	}

	Matrix(int[][] mat) {
		data = new int[4][4];
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				this.data[row][col] = mat[row][col];
			}
		}
	}

	public Matrix matmul(Matrix matrix) {
		Matrix result = new Matrix();

		for (int row = 0; row < result.len[0]; row++) {
			for (int col = 0; col < result.len[1]; col++) {
				result.data[row][col] = 0;
				for (int k = 0; k < result.len[1]; k++) {
					//System.out.println(this.data[row][col] + "," + this.data[row])
					result.data[row][col] += (this.data[row][k] * matrix.data[k][col]);
				}
			}
		}

		return result;
	}

	public String toString() {
		String res = "";
		for (int row = 0; row < this.len[0]; row++) {
			res += "[";
			for (int col = 0; col < this.len[1]; col++) {
				res += this.data[row][col] + ", ";
			}
			res += "]";
			res += "\n";
		}
		return res;
	}

	public static void main(String args[]) {

		int[][] dat1 = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
		int[][] dat2 = {{20, 21, 22, 23}, {24, 25, 26, 27}, {28, 29, 30, 31}, {32, 33, 34, 35}}; 

		Matrix a = new Matrix(dat1);
		Matrix b = new Matrix(dat2);

		Matrix c = a.matmul(b);

		System.out.println(c);
	}

}
