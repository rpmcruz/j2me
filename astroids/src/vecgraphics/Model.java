// Astroids (J2ME) - (C) 2011 Ricardo Cruz <rpmcruz0@gmail.com>

package vecgraphics;
import astroids.MyCanvas;
import javax.microedition.lcdui.Graphics;

public class Model {
    private Vector points[];
    private float scale = 1;
    
    public Model(int size) {
        points = new Vector[size];
    }

    public void setPoint(int n, Vector p) {
        points[n] = p;
    }
    
    public void setScale(float scale) {
        this.scale = scale;
    }

    public void paint(Graphics g, Vector pos, float angle) {
		Vector p0 = transform(points[0], pos, angle);
		Vector _v = p0;
		for(int i = 1; i < points.length; i++) {
            Vector v = transform(points[i], pos, angle);
			g.drawLine((int)_v.x, (int)_v.y, (int)v.x, (int)v.y);
			_v = v;
        }
		g.drawLine((int)_v.x, (int)_v.y, (int)p0.x, (int)p0.y);
    }

	public Vector transform(Vector v, Vector pos, float angle) {
		float x = (float) ((v.x * Math.cos(angle)) + (v.y * Math.sin(angle))) * scale * MyCanvas.scale;
		float y = (float) ((v.x * Math.sin(angle)) - (v.y * Math.cos(angle))) * scale * MyCanvas.scale;
		return new Vector(x + pos.x, y + pos.y);
	}
}
