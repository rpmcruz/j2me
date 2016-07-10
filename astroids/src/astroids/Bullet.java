// Astroids (J2ME) - (C) 2011 Ricardo Cruz <rpmcruz0@gmail.com>

package astroids;
import libs.VectorSprite;
import vecgraphics.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class Bullet extends VectorSprite
{
	static private final int SIZE = 4;
	private int cycles = 0;
	
	public Bullet(Vector pos, float angle) {
		super((int)pos.x, (int)pos.y);
		this.angle = angle;
		model = new Model(4);
		model.setPoint(0, new Vector(+1, +.2f));
		model.setPoint(1, new Vector(+1, -.2f));
		model.setPoint(2, new Vector(-1, -.2f));
		model.setPoint(3, new Vector(-1, +.2f));
		model.setScale(SIZE);
	}
	
	public void update() {
		forward(5.f);
		cycles++;
	}
	
	public boolean isAlive() {
		return cycles < 20;
	}
}
