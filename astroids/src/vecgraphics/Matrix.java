/**
 * Astroids clone.
 *
 * @author  Ricardo Cruz
 * @version 0.1
 * @license GNU GPL v3.0
 */

package vecgraphics;

public class Matrix
{
	// matrix indicies:
	// ( 0 1 | 4 )
	// ( 2 3 | 5)
    private float m[];
    	
	public Matrix(Matrix other) {
		m = new float[6];
		System.arraycopy(other.m, 0, this.m, 0, 6);
	}

	public Matrix(float []m) {
		this.m = m;
	}
	
	static public Matrix createIdentity() {
		float []m = new float[6];
		m[0] = m[3] = 1;
		m[1] = m[2] = m[4] = m[5] = 0;
		return new Matrix(m);
	}

	static public Matrix createRotation(float angle) {
		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float m[] = new float[6];
		m[0] = c;
		m[1] = -s;
		m[2] = s;
		m[3] = c;
		m[4] = m[5] = 0;
System.out.println("** rotation matrix: ( ( " + m[0] + " " + m[1] + " ) ( " + m[2] + " " + m[3] + " ) )");
		return new Matrix(m);
	}

	static public Matrix createScaling(float xscale, float yscale) {
		float []m = new float[6];
		m[0] = xscale;
		m[3] = yscale;
		m[1] = m[2] = m[4] = m[5] = 0;
System.out.println("** scaling matrix: ( ( " + m[0] + " " + m[1] + " ) ( " + m[2] + " " + m[3] + " ) )");
		return new Matrix(m);
	}

	static public Matrix createTranslation(float dx, float dy) {
		float []m = new float[6];
		m[0] = m[3] = 1;
		m[1] = m[2] = 0;
		m[4] = dx;
		m[5] = dy;
System.out.println("** translation matrix: ( ( " + m[0] + " " + m[1] + " ) ( " + m[2] + " " + m[3] + " ) )");
		return new Matrix(m);
	}

	public void multiply(Matrix o) {
		float m0 = m[0], m2 = m[2];
		m[0] = (m0   * o.m[0]) + (m[1] * o.m[2]);
		m[1] = (m0   * o.m[1]) + (m[1] * o.m[3]);
		m[2] = (m2   * o.m[0]) + (m[3] * o.m[2]);
		m[3] = (m2   * o.m[1]) + (m[3] * o.m[3]);
		m[4] += o.m[4];
		m[5] += o.m[5];
System.out.println("** multiply result: ( ( " + m[0] + " " + m[1] + " ) ( " + m[2] + " " + m[3] + " ) )");
	}
	
	public Vector transform(Vector v) {
		float x = (v.x * m[0]) + (v.y * m[1]) + m[4];
		float y = (v.x * m[2]) + (v.y * m[3]) + m[5];
System.out.println("apply to vector (" + v.x + " , " + v.y + ") - matrix: ( ( " + m[0] + " " + m[1] + " ) ( " + m[2] + " " + m[3] + " ) )  =  " + x + " , " + y);
		return new Vector((int)x, (int)y);
	}
}
