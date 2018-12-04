package world;

import misc.Vector;

import java.io.Serializable;

public class TriangleLight extends Triangle implements Serializable {
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
