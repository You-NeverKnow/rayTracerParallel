package miscellaneous;

import World.World;
import edu.rit.image.Color;

public class Phong {
    Vector ka = new Vector();
    Vector kd = new Vector();
    Vector ks = new Vector();

    double ke;
    double kr;
    double kt;
    double depth;

    public Phong() {
    }

    public void set(Vector ka, Vector kd, Vector ks, double ke,
                 double kr, double kt, double depth) {
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

    public Color computeRadiance(IntersectionData hitData, World world) {
        // Max color
        Vector maxColor = new Vector(3, 3, 3);
        // World ambient color
        Color ambient_color = new Color().rgb(1f, 1f, 1f);

        Vector ambient = this._indexWiseMultiply(
                                            ambient_color, hitData.color, ka);

        // Get diffuse and specular color
        Vector diffuse = new Vector();
        Vector specular = new Vector();

        // Calculate v
//        System.out.println("Hit intersection dir = " + hitData.intersectionDirection);
//        System.out.println("Hit intersection point = " + hitData.intersectionPoint);
        Vector v = hitData.intersectionDirection.multiply(-1);
//        System.out.println("v = " + v);
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

            Vector r = this._getReflectedRay(s.multiply(-1), hitData.normal);
            r.normalize();

            diffuse.selfAdd(
                    this._indexWiseMultiply(
                            white, hitData.color, kd).multiply(
                                    s.dot(hitData.normal)));

//            System.out.println(Math.pow(r.dot(v), ke));
//            System.out.println("Index wise mul = " + this._indexWiseMultiply(
//                            white, white, ks));
            specular.selfAdd(
                    this._indexWiseMultiply(
                            white, white, ks).multiply(Math.pow(r.dot(v), ke)));


//            System.out.println("r.dot(v) = " + r.dot(v));
//            System.out.println("r = " + r);
//            System.out.println("v = " + v);
//            System.out.println("Math.pow(r.dot(v), ke) = " + Math.pow(r.dot(v), ke));

        }

//        System.out.println("Colors: ");
//        System.out.println(ambient);
//        System.out.println(diffuse);
//        System.out.println(specular);
//        System.out.println();
//        if (specular.x > 3f || specular.y > 3f || specular.z > 3f) {
//            System.out.println("Yes");
//            System.out.println(specular);
//        }
//
//        if (diffuse.x > 3f || diffuse.y > 3f || diffuse.z > 3f) {
//            System.out.println("Yes diffuse");
//        }
//        if (ambient.x > 3f || ambient.y > 3f || ambient.z > 3f) {
//            System.out.println("Yes ambient");
//        }


        return this.convertVectorColor(ambient.add(specular.add(diffuse)), maxColor);
    }

    private Vector _getReflectedRay(Vector s, Vector n) {
        return s.subtract(n.multiply(2 * s.dot(n)));
    }

    private Vector _indexWiseMultiply(Color ambient_color, Color color, Vector ka) {

        double r = (ambient_color.red()/256f * color.red()/256f * ka.x);
        double g = (ambient_color.blue()/256f * color.green()/256f * ka.y);
        double b = (ambient_color.blue()/256f * color.green()/256f * ka.z);

        return new Vector(r, g, b);
    }

    private Color convertVectorColor(Vector v, Vector m) {

        Color rgb = new Color().rgb((float) (v.x / m.x),
                (float) (v.y / m.y),
                (float) (v.z / m.z));

//        System.out.println("color = " + rgb);
        return rgb;
    }

}
