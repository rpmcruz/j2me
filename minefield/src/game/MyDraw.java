// Minefield - (C) 2005-2012 Ricardo Cruz <ric8cruz@gmail.com>

/*
 * MVC-model:
 * - MinesDisplay: controller
 * - MinesView: view
 * - MinesData: model
 * 
 * The view only draws stuff: it is not derived from Displayable (or
 * Canvas). It is purely an abstract class; the controller is the actual
 * concrete object.
 */

package game;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;

public class MyDraw
{
	private int TILE_SIZE, BORDER_SIZE = 2;
	private Font msg = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
	private Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
	private Sprite smile;

	public int scroll_x = 0, scroll_y = 0;

	public MyDraw() {
		Image img = null;
		try { img = Image.createImage("/smile.png"); } catch(Exception ex) { }
		smile = new Sprite(img, 24, 24);
		smile.defineReferencePixel(12, 12);
		
		int w = font.stringWidth("2"), h = font.getHeight();
		TILE_SIZE = ((w > h) ? w : h) + 6;
		// higher resolutions:
		if(font.getHeight() > 14) {
			BORDER_SIZE = 4;
			TILE_SIZE += 2;
		}
	}

	public void paint(Graphics g, Displayable disp, MyData data, int cursor_x, int cursor_y) {
		int screenWidth = disp.getWidth(), screenHeight = disp.getHeight();
		MySettings s = MySettings.get();

		if(screenWidth < s.width*TILE_SIZE) {  // only scroll if needed
			if(scroll_x > (cursor_x-2)*TILE_SIZE)
				scroll_x = Math.max(0, (int)((cursor_x-2.5)*TILE_SIZE));
			if(scroll_x < (cursor_x+3)*TILE_SIZE - screenWidth)
				scroll_x = Math.min((int)((cursor_x+3.5)*TILE_SIZE), s.width*TILE_SIZE) - screenWidth;
		}
		if(screenHeight < s.height*TILE_SIZE) {
			if(scroll_y > (cursor_y-2)*TILE_SIZE)
				scroll_y = Math.max(0, (int)((cursor_y-2.5)*TILE_SIZE));
			if(scroll_y < (cursor_y+3)*TILE_SIZE - screenHeight)
				scroll_y = Math.min((int)((cursor_y+3.5)*TILE_SIZE), s.height*TILE_SIZE) - screenHeight;
		}
/*
		g.setColor(0xffffff);
		g.fillRect(0, 0, screenWidth, screenHeight);
*/
		int x, y;
		for(x = scroll_x/TILE_SIZE; x < s.width && (x*TILE_SIZE)-scroll_x < screenWidth; x++)
			for(y = scroll_y/TILE_SIZE; y < s.height && (y*TILE_SIZE)-scroll_y < screenHeight; y++)
				paintTile(data, g, x, y);

		paintCursor(g, cursor_x, cursor_y);

		if(data.isGameover()) {
			String title = data.status == MyData.WON ? "You're the Man !" : "Loooooser";
			String text1 = "Time: " + formatTime(data.timeElapsed());
			String text2 = "";
			boolean highlight = data.newHighscore();
			if(data.status == MyData.WON && data.prevHighscore != 0) {
				if(highlight)
					text2 += "Beats: ";
				else
					text2 += "Best: ";
				text2 += formatTime(data.prevHighscore);
			}
			drawMessage(g, disp, title, text1, text2,
					data.status == MyData.WON, data.newHighscore());
		}
    }

	public void paintTile(MyData data, Graphics g, int x, int y) {
		int sx = (x*TILE_SIZE)-scroll_x;
		int sy = (y*TILE_SIZE)-scroll_y;
		boolean uncovered = data.isUncovered(x,y);
		drawButton(g, sx, sy, TILE_SIZE, TILE_SIZE, uncovered);

		boolean gameOver = data.isGameover();

		if(data.isFlagged(x,y)) {
			drawFlag(g, sx, sy);

			// on game over, cross badly placed flags
			if(data.isGameover() && !data.hasMine(x,y))
				drawCross(g, sx, sy);
		}
		else if(data.hasMine(x,y) && data.isGameover())
			drawMine(g, sx, sy, data.isUncovered(x,y));

		if(data.isUncovered(x,y) && data.getTile(x,y) != 0) {
			switch(data.getTile(x,y)) {
				case 1: g.setColor(0,0,255); break;
				case 2: g.setColor(0, 140,0); break;
				case 3: g.setColor(140, 140,0); break;
				case 4: g.setColor(140, 0, 140); break;
				case 5: g.setColor(255, 0, 0); break;
				case 6: g.setColor(140, 0, 0); break;
				default: g.setColor(0,0,0); break;
			}
			String s = String.valueOf(data.getTile(x, y));
			int w = font.stringWidth(s);
			int h = font.getHeight();
			g.setFont(font);
			g.drawString(s, sx + (TILE_SIZE-w)/2, sy + (TILE_SIZE-h)/2,
					Graphics.TOP|Graphics.LEFT);
		}
	}

	private void paintCursor(Graphics g, int cursor_x, int cursor_y) {
		final int b = BORDER_SIZE;
		g.setColor(0,0,0);
		g.setStrokeStyle(Graphics.DOTTED);
		g.drawRect((cursor_x*TILE_SIZE)+b-scroll_x, (cursor_y*TILE_SIZE)+b-scroll_y, TILE_SIZE-5, TILE_SIZE-5);
		g.drawRect((cursor_x*TILE_SIZE)+b+1-scroll_x, (cursor_y*TILE_SIZE)+b+1-scroll_y, TILE_SIZE-7, TILE_SIZE-7);
	}

	private void drawFlag(Graphics g, int x, int y) {
		x += BORDER_SIZE; y += BORDER_SIZE;
		int w = TILE_SIZE-(BORDER_SIZE*2), h = w;
		g.setColor(0,0,0);
		g.fillRect(x+w-3, y+1, 2, h-1);  // mastro
		g.fillRect(x+4, y+h-2, w-4, 2);  // base
		g.setColor(255,0,0);
		g.fillRect(x+1, y+1, w-4, 6 /*h/3*/);  // body
	}

	public void drawCross(Graphics g, int x, int y) {
		final int b = BORDER_SIZE, b2 = b*2;
		g.setColor(220,0,0);
		g.drawLine(x+b, y+b, x+TILE_SIZE-b2, y+TILE_SIZE-b2);
		g.drawLine(x+TILE_SIZE-b2, y+b, x+b, y+TILE_SIZE-b2);  // cross
		g.drawLine(x+b+1, y+b, x+TILE_SIZE-b+1, y+TILE_SIZE-b2);
		g.drawLine(x+TILE_SIZE-b+1, y+b, x+b+1, y+TILE_SIZE-b2);  // just to make it bolder
	}
	
	public void drawMine(Graphics g, int x, int y, boolean pressed) {
		final int b = BORDER_SIZE, b2 = b*2;
		if(pressed) {  // yellow background
			g.setColor(255,255,0);
			g.fillRect(x+b, y+b, TILE_SIZE-b2, TILE_SIZE-b2);
		}
		g.setColor(0,0,0);
		g.fillArc(x+(TILE_SIZE/4), y+(TILE_SIZE/3), TILE_SIZE/2, TILE_SIZE/2, 0, 360);
		g.drawArc(x+(TILE_SIZE/2), y+(TILE_SIZE/6), TILE_SIZE/2, TILE_SIZE/2, 180, -70); // fusivel
	}

	public void drawButton(Graphics g, int x, int y, int w, int h, boolean pressed) {
		final int b = BORDER_SIZE, b2 = b*2;
		// draw the square
		g.setColor(200,200,200);
		g.fillRect(x+b, y+b, TILE_SIZE-b2, TILE_SIZE-b2);
		// draw the shadow
		if(pressed)
		  g.setColor(80,80,80);
		else
		  g.setColor(255,255,255);
		g.fillRect(x, y, w, b);
		g.fillRect(x, y, b, h);
		if(pressed)
		  g.setColor(255,255,255);
		else
		  g.setColor(80,80,80);
		g.fillRect(x-b, y+h-b, w, 2);
		g.fillRect(x+w-b, y-b, 2, h);
	}

	static public String formatTime(int time) {
		if(time == 0) return "--";
		int mins = time / 60;
		int secs = time % 60;
		String str = "";
		if(mins > 0)
			str += mins + "m ";
		if(secs > 0)
			str += secs + "s";
		return str;
	}

	public void drawMessage(Graphics g, Displayable disp, String title, String text1, String text2, boolean happy, boolean highlight) {
		int screenWidth = disp.getWidth(), screenHeight = disp.getHeight();
		int strWidth = msg.stringWidth(title.length() > text1.length() ? title : text1);
		int strHeight = msg.getHeight()*3 + 10;

		int _x = (screenWidth-strWidth)/2 - 20 - 18;
		int _y = (screenHeight-strHeight)/2 - 2;
		int _w = strWidth + 40 + 18;
		int _h = strHeight + 6;
		g.setColor(150, 150, 150);
		g.fillRect(_x+3, _y+3, _w, _h);
		g.setColor(0, 0, 0);
		g.fillRect(_x, _y, _w, _h);

		_y = (screenHeight-strHeight)/2;
		g.setColor(255, 255, 255);
		g.setFont(msg);
		g.drawString(title,
				screenWidth/2, _y,
				Graphics.TOP|Graphics.HCENTER);
		if(highlight)
			g.setColor(255,255,0);
		g.drawString(text1,
				screenWidth/2, _y + msg.getHeight() + 4,
				Graphics.TOP|Graphics.HCENTER);
		g.drawString(text2,
				screenWidth/2, _y + msg.getHeight()*2 + 8,
				Graphics.TOP|Graphics.HCENTER);

		smile.setFrame(happy ? 0 : 1);
		smile.setRefPixelPosition(_x+20, _y+20);
		smile.paint(g);
	}
}
