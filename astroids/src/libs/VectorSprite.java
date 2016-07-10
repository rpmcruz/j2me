package libs;
import javax.microedition.lcdui.Graphics;
import vecgraphics.*;

public abstract class VectorSprite
{
	static private final int SIZE = 8;
	protected Model model;
	protected Vector pos;
	protected float angle;
	
	public VectorSprite(int x, int y) {
		pos = new Vector(x, y);
	}
	
	public void setPos(int x, int y) {
		pos = new Vector(x, y);
	}

	public void forward(float vel) {
		pos.x += vel * Math.cos(angle);
		pos.y += vel * Math.sin(angle);
	}
	
	public void rotate(float angular_vel) {
		angle += angular_vel;
	}
	
	public void move(int dx, int dy) {
		pos.x += dx;
		pos.y += dy;
	}

	public Vector getPos() {
		return pos;
	}
	
	public float getAngle() {
		return angle;
	}	
	
	public void paint(Graphics g) {
//		g.setColor(0, 0, 0);
		model.setScale(SIZE);
		model.paint(g, pos, angle);
		model.setScale(SIZE+1);
		model.paint(g, pos, angle);
		model.setScale(SIZE+2);
		model.paint(g, pos, angle);
	}

	public void paintAllSides(Graphics g, int dispWidth, int dispHeight) {
		// HACK: draw on the other side ... :p
		VectorSprite.this.paint(g);
		
		if(pos.x+40 > dispWidth) {
			move(-dispWidth, 0);
			VectorSprite.this.paint(g);
			move(+dispWidth, 0);
		}
		if(pos.x-40 < 0) {
			move(+dispWidth, 0);
			VectorSprite.this.paint(g);
			move(-dispWidth, 0);
		}
		if(pos.y+40 > dispHeight) {
			move(-dispHeight, 0);
			VectorSprite.this.paint(g);
			move(+dispHeight, 0);
		}
		if(pos.x-40 < 0) {
			move(+dispHeight, 0);
			VectorSprite.this.paint(g);
			move(-dispHeight, 0);
		}
	}
	
	public boolean collides(VectorSprite other) {
		final int DIST = SIZE*2;
		return pos.getSqDistance(other.pos) <= DIST*DIST;
	}
	
	public void insideScreen(int dispWidth, int dispHeight) {
		while(pos.x >= dispWidth)
			pos.x -= dispWidth;
		while(pos.x < 0)
			pos.x += dispWidth;
		while(pos.y >= dispHeight)
			pos.y -= dispHeight;
		while(pos.y < 0)
			pos.y += dispHeight;
	}
}
