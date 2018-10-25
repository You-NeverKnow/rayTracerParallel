package miscellaneous;

public class Vector {
	public double x;
	public double y;
	public double z;

	Vector() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(double arr[]) {
		this.x = arr[0];
		this.y = arr[1];
		this.z = arr[2];
	}

	public Vector add(Vector point) {
		return new Vector(this.x + point.x,
						this.y + point.y,
						this.z + point.z);
	}

	public Vector multiply(float k) {
		return new Vector(this.x * k,
						this.y * k,
						this.z * k);
	}

	public double norm() {
		return Math.sqrt(x*x + y*y + z*z);
	}

	public double dot(Vector b) {
		return (this.x * b.x) + (this.y * b.y) + (this.z * b.z);
	}

	public Vector cross(Vector b) {
		double x =   (this.y * b.z) - (b.y * this.z);
		double y = - ((this.x * b.z) - (b.x * this.z));
		double z =   (this.x * b.y) - (b.x * this.y);

		return new Vector(x, y, z);
	}

	public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	public void normalize() {
        double normValue = norm();
	    x /= normValue;
	    y /= normValue;
	    z /= normValue;
	}

	public Vector subtract(Vector lookAt) {
		return new Vector(this.x - lookAt.x, this.y - lookAt.y,
				this.z - lookAt.z);
	}

    public void set(Vector other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;

    }

    public void transform(double[][] transformMatrix) {
//	    double x =    this.x * transformMatrix[0][0] +
//                    this.y * transformMatrix[1][0] +
//                    this.z * transformMatrix[2][0] +
//                    transformMatrix[3][0];
//
//	    double y =    this.x * transformMatrix[0][1] +
//                    this.y * transformMatrix[1][1] +
//                    this.z * transformMatrix[2][1] +
//                    transformMatrix[3][1];
//
//	    double z =    this.x * transformMatrix[0][2] +
//                    this.y * transformMatrix[1][2] +
//                    this.z * transformMatrix[2][2] +
//                    transformMatrix[3][2];
//
//	    this.set(x, y, z);
//        System.out.println("" + transformMatrix);

//        // Debug
//        for (int i = 0; i < transformMatrix.length; i++) {
//            for (int j = 0; j < transformMatrix[i].length; j++) {
//                System.out.printf("mat(%d, %d) = %1.2f", i, j, transformMatrix[i][j]);
//                System.out.println();
//            }
//        }
//        System.out.println();
        double vec[] = {x, y, z, 1};
        double result[] = {0, 0, 0};

        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 4; row++) {
                result[col] += vec[row] * transformMatrix[row][col];
            }
        }

        this.set(result);
    }
}
