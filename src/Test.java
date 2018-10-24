/**
 * Test
 */
public class Test {

    public static void main(String[] args) {
        double[][] t = new double[][] {
            {-1, 0, 0, 0},
            {0, -1, 0, 0},
            {0, 0, -1, 0},
            {75, 60, 0, 1}
        };

        // transform(t);
        Vector a = new Vector(0, 0, 1);
        Vector b = new Vector(1, 0, 0);
        a.cross(b);

    };
    public static void transform(double[][] transformMatrix) {
        double vec[] = {85, 80, 20, 1};
        double result[] = {0, 0, 0};

        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 4; row++) {
                result[col] += vec[row] * transformMatrix[row][col];
            }
        }

        System.out.printf("(x, y, z) = %f, %f, %f", result[0], result[1], result[2]);
        System.out.println();

    }
        
}

 class Vector {
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
    public void cross(Vector b) {
		double x =   (this.y * b.z) - (b.y * this.z);
		double y = - (this.x * b.z) - (b.x * this.z);
		double z =   (this.x * b.y) - (b.x * this.y);
        // System.out.print((this.x * b.z) );
        
        System.out.printf("(x, y, z) = %f, %f, %f", x, y, z);
        System.out.println();

    }

}

