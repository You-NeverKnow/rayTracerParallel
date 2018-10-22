package miscellaneous;

public class Vector {
	public float x;
	public float y;
	public float z;
	public float w;

	public Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1;
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

	public Vector normalize() {
		float norm = (float) Math.sqrt(x*x + y*y + z*z);
		return new Vector(this.x/norm, this.y/norm, this.z/norm);
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

	public Vector subtract(Vector otherVector) {

		return new Vector(this.x - otherVector.x,
				this.y- otherVector.y,
				this.z - otherVector.z);
	}
}
