package miscellaneous;

import edu.rit.image.Color;

public class Vector {
	public double x;
	public double y;
	public double z;

	public Vector() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static Vector _getRefractedRay(Vector s, Vector normal,
										  double relativeRefractionIndex) {

		double sDotNSquared = Math.pow(s.dot(normal), 2);
		double rriSquared = Math.pow(relativeRefractionIndex, 2);

		double discriminant = 1 - (rriSquared * (1 - sDotNSquared));

		if (discriminant < 0) {
			return null;
		}

		Vector a = s.subtract(normal.multiply(s.dot(normal)));
		Vector b = normal.multiply(Math.sqrt(discriminant));
		return a.multiply(relativeRefractionIndex).add(b);
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

    public void selfAdd(Vector point) {
		this.x += point.x;
        this.y += point.y;
        this.z += point.z;
	}

	public Vector multiply(double k) {
		return new Vector(this.x * k,
						this.y * k,
						this.z * k);
	}

	public void selfMultiply(double k) {
		this.x *= k;
		this.y *= k;
		this.z *= k;
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
        return "(" + String.format("%.2f", x) + ", " +
                String.format("%.2f", y) + ", " +
                String.format("%.2f", z) + ")";
	}

	public void normalize() {
        double normValue = this.norm();
	    this.x /= normValue;
	    this.y /= normValue;
	    this.z /= normValue;
	}

	public Vector subtract(Vector lookAt) {
		return new Vector(this.x - lookAt.x, this.y - lookAt.y,
				this.z - lookAt.z);
	}

    public void selfSubtract(Vector lookAt) {
	    this.x -= lookAt.x;
	    this.y -= lookAt.y;
	    this.z -= lookAt.z;
	}

    public void set(Vector other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;

    }

    public void transform(double[][] transformMatrix) {
        double vec[] = {x, y, z, 1};
        double result[] = {0, 0, 0};

        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 4; row++) {
                result[col] += vec[row] * transformMatrix[row][col];
            }
        }

        this.set(result);
    }

	public static Vector _getReflectedRay(Vector s, Vector n) {
        return s.subtract(n.multiply(2 * s.dot(n)));
    }

	public static Color _convertVectorColor(Vector v, Vector m) {

        Color rgb = new Color().rgb((float) (v.x / m.x),
                (float) (v.y / m.y),
                (float) (v.z / m.z));

        return rgb;
    }


}
