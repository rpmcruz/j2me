// My J2ME auxiliary module - (C) 2011 Ricardo Cruz <rpmcruz0@gmail.com>

/*
 * Does sprite rotation using as feed an image of 5 frames
 * of the object rotated in steps of 18º (1st quadrant).
 */

package libs;
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class RotateSprite extends Sprite
{
	private static final int FRAMES = 5;
	private double x, y, angle = 0;
	
	public RotateSprite(Image img, int size) {
		super(img, size, size);
		defineReferencePixel(size/2, size/2);
		updateFrame();
	}

	public double getAngle() { return angle; }
	
	public void move(double vel) {
		x += vel * Math.cos(angle);
		y -= vel * Math.sin(angle);
		setRefPixelPosition((int)x, (int)y);
	}
	
	public void turn(double angular_vel) {
		angle += angular_vel;
		angle = MyMath.normalizeAngle(angle);
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		super.setPosition((int) x, (int) y);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		super.setPosition(x, y);
	}

	public final void updateFrame() {
		double frame_angle = (angle / (Math.PI*2)) * FRAMES * 4;  // [0,20]
		frame_angle += .5;
		int frame = (int) (frame_angle % FRAMES);  // [0,5]

		int transform = Sprite.TRANS_NONE;
		int quadrant = (int) (frame_angle / FRAMES);
		switch(quadrant) {
			case 1: transform = Sprite.TRANS_ROT270; break;
			case 2: transform = Sprite.TRANS_ROT180; break;
			case 3: transform = Sprite.TRANS_ROT90; break;
		}
		setFrame(frame);
		setTransform(transform);
	}
}
