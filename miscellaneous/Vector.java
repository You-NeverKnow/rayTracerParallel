package miscellaneous;

public class Vector {
	float x;
	float y;
	float z;
	float w;

	Vector() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 1;
	}

	Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1;
	}

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1;
	}

	public void set(float arr[]) {
		this.x = arr[0];
		this.y = arr[1];
		this.z = arr[2];
		this.w = arr[3];
	}

	public Vector add(Vector point) {
		return new Vector(this.x + point.x,
						this.y + point.y,
						this.z + point.z);
	}

	public Vector sub(Vector point) {
		return new Vector(this.x - point.x,
						this.y - point.y,
						this.z - point.z);
	}

	public Vector multiply(float k) {
		return new Vector(this.x * k,
						this.y * k,
						this.z * k);
	}

	public float norm() {
		return (float) Math.sqrt(x*x + y*y + z*z);
	}

	public float dot(Vector b) {
		return (this.x * b.x) + (this.y * b.y) + (this.z * b.z);
	}

	public Vector cross(Vector b) {
		float x =   (this.y * b.z) - (b.y * this.z);
		float y = - ((this.x * b.z) - (b.x * this.z));
		float z =   (this.x * b.y) - (b.x * this.y);

		return new Vector(x,y,z);
	}

	public String toString() {
		String res = "(" + this.x + "," + this.y + "," + this.z + this.w + ")";
		return res;
	}

}
