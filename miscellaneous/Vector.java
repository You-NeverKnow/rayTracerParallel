package miscellaneous;

public class Vector {
    float x;
    float y;
    float z;
    float w;

    Vector(float x, float y, float z) {
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

}
