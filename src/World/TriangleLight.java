package World;

import miscellaneous.Vector;

public class TriangleLight extends Triangle{
    public Vector direction;

    public TriangleLight(Vector vertex1, Vector vertex2, Vector vertex3, Vector direction) {
        super(vertex1, vertex2, vertex3);
        this.direction = direction;
    }

    @Override
    public void transform(double[][] transformMatrix) {
        super.transform(transformMatrix);
        this.direction.transform(transformMatrix);
    }

}
