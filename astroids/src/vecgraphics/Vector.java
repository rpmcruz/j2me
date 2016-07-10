/**
 * Astroids clone.
 *
 * @author  Ricardo Cruz
 * @version 0.1
 * @license GNU GPL v3.0
 */

package vecgraphics;

public class Vector
{
    public float x, y;
    
    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

	public int getLength() {
		return (int) Math.sqrt((x*x) + (y*y));
	}
	/*
	public void normalize() {
		float l = getLength();
		x /= l;
		y /= l;
	}
	*/
	public float getSqDistance(Vector other) {
		float dx = this.x - other.x;
		float dy = this.y - other.y;
		return (dx*dx) + (dy*dy);
	}
}
