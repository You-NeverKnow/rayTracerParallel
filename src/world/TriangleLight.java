package world;

import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import misc.Vector;

import java.io.IOException;

public class TriangleLight extends Triangle {
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

    @Override
    public void writeOut(OutStream out) throws IOException {
        super.writeOut(out);
        out.writeObject(direction);
    }

    @Override
    public void readIn(InStream in) throws IOException {
        super.readIn(in);
        direction = (Vector)in.readObject();
    }
}
