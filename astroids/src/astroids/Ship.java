// Astroids J2ME - (C) 2011-2012 Ricardo Cruz <ric8cruz@gmail.com>

package astroids;
import libs.VectorSprite;
import vecgraphics.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class Ship extends VectorSprite
{
	static private final int SIZE = 2;
	private float vel = 0;

	public Ship(int x, int y) {
		super(x, y);
		model = new Model(4);
		model.setPoint(0, new Vector(+1,  0));
		model.setPoint(1, new Vector(-1, +1));
		model.setPoint(2, new Vector(-.65f, 0));
		model.setPoint(3, new Vector(-1, -1));
		model.setScale(SIZE);
	}
	
	public void processKeys(int keys) {
		if ((keys & GameCanvas.LEFT_PRESSED) != 0)
			rotate(-.10f);
		if ((keys & GameCanvas.RIGHT_PRESSED) != 0)
			rotate(+.10f);
		if ((keys & GameCanvas.UP_PRESSED) != 0)
			vel += .35f;
		if ((keys & GameCanvas.DOWN_PRESSED) != 0)
			vel -= .35f;
		forward(vel);
	}
}
