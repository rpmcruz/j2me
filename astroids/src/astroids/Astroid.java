// Astroids J2ME - (C) 2011-2012 Ricardo Cruz <ric8cruz@gmail.com> //

package astroids;
import libs.VectorSprite;
import vecgraphics.*;
import javax.microedition.lcdui.*;
import libs.MyRandom;

public class Astroid extends VectorSprite
{
	static private final int SIZE = 2;
	private float paint_angle = 0;
	
	public Astroid(int x, int y) {
		super(x, y);
		model = new Model(8);
		model.setPoint(0, new Vector(-.2f,  -1));
		model.setPoint(1, new Vector(+1, -.75f));
		model.setPoint(2, new Vector(+.8f, +.25f));
		model.setPoint(3, new Vector(+1, .7f));
		model.setPoint(4, new Vector(.2f, 1));
		model.setPoint(5, new Vector(-1, .8f));
		model.setPoint(6, new Vector(-.65f, .1f));
		model.setPoint(7, new Vector(-1, -.1f));
		model.setScale(SIZE);
		float angle = (float)(MyRandom.get().nextFloat() * Math.PI*2);
		rotate(angle);
	}
	
	public void update() {
		forward(2.f);
		paint_angle += 0.01f;
	}

	public void paint(Graphics g, int dispWidth, int dispHeight) {
		rotate(paint_angle);

		paintAllSides(g, dispWidth, dispHeight);

		rotate(-paint_angle);
	}
}
