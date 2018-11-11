package miscellaneous;
import edu.rit.image.Color;

public class Phong {
    Vector ka = new Vector();
    Vector kd = new Vector();
    Vector ks = new Vector();

    double ke;
    public double kr;
    public double kt;
    public int depth;

    public Phong() {
    }

    public void set(Vector ka, Vector kd, Vector ks, double ke,
                    double kr, double kt, int depth) {
        this.ka.set(ka);
        this.kd.set(kd);
        this.ks.set(ks);
        this.ke = ke;
        this.kr = kr;
        this.kt = kt;
        this.depth = depth;
    }

    public void set(Phong phong) {
        this.ka.set(phong.ka);
        this.kd.set(phong.kd);
        this.ks.set(phong.ks);

        this.ke = phong.ke;
        this.kr = phong.kr;
        this.kt = phong.kt;
        this.depth = phong.depth;
    }

    public Vector computeRadiance(IntersectionData hitData) {
        // World ambient color
        Color ambient_color = new Color().rgb(1f, 1f, 1f);

        Vector ambient = this._indexWiseMultiply(
                                            ambient_color, hitData.color, ka);

        // Get diffuse and specular color
        Vector diffuse = new Vector();
        Vector specular = new Vector();

        // Calculate v
        Vector v = hitData.intersectionDirection.multiply(-1);
        v.normalize();

        Color white = new Color().rgb(1f,1f, 1f);

        // Add colors from all lights
        for (Vector light: hitData.lights)
        {
            if (light == null) {
                continue;
            }

            Vector s = light.subtract(hitData.intersectionPoint);
            s.normalize();

            Vector r = Vector._getReflectedRay(s.multiply(-1), hitData.normal);
            r.normalize();

            diffuse.selfAdd(
                    this._indexWiseMultiply(
                            white, hitData.color, kd).multiply(
                                    s.dot(hitData.normal)));

            specular.selfAdd(
                    this._indexWiseMultiply(
                            white, white, ks).multiply(Math.pow(r.dot(v), ke)));

        }

        return ambient.add(specular.add(diffuse));
    }

    private Vector _indexWiseMultiply(Color ambient_color, Color color, Vector ka) {

        double r = (ambient_color.red()/256f * color.red()/256f * ka.x);
        double g = (ambient_color.blue()/256f * color.green()/256f * ka.y);
        double b = (ambient_color.blue()/256f * color.green()/256f * ka.z);

        return new Vector(r, g, b);
    }

}
