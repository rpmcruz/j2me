// Microracers (J2ME) - (C) 2011 Ricardo Cruz <rpmcruz0@gmail.com>

package game;
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;

public class Car extends libs.RotateSprite
{
	public int nextCheckpoint = 1, checkpoints = 0, ranking;
	
	// cache checkpoint position (needed for the computer AI)
	public int nextCheckpointX = 0, nextCheckpointY = 0;
	
	public Image image;

	public Car(Image img) {
		super(img, 16);
		image = img;
		setPosition(40, 40);
	}
	
	public void drawIcon(Graphics g, int x, int y) {
		g.drawRegion(image, 0, 0, 16, 16, Sprite.TRANS_NONE, x, y, 0);
	}
}
