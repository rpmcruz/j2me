// My J2ME auxiliary module - (C) 2011 Ricardo Cruz <rpmcruz0@gmail.com>

package libs;
import javax.microedition.lcdui.Graphics;

public class PaintForm
{
	public static void drawButton(Graphics g, int x, int y, int w, int h, boolean pressed) {
		final int back = 95, light = 80, dark = 120;

		// draw the square
		g.setColor(back, back, back);
		g.fillRect(x+2, y+2, w-4, h-4);
		// draw the shadow
		if(pressed)
		  g.setColor(light, light, light);
		else
		  g.setColor(dark, dark, dark);
		g.fillRect(x, y, w, 2);
		g.fillRect(x, y, 2, h);
		if(pressed)
		  g.setColor(dark, dark, dark);
		else
		  g.setColor(light, light, light);
		g.fillRect(x+2, y+h-2, w-2, 2);
		g.fillRect(x+w-2, y+2, 2, h-2);
    }	
}
