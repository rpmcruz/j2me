// Gryzzles J2ME - (C) 2011-2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;
import javax.microedition.lcdui.*;

public class MyCanvas extends Canvas
{
	private Memory memory;
	private final int W;
	private Level level;
	private int x, y;

	public MyCanvas() {
        super();
		memory = new Memory("GryzzlesLevel", 1);
		loadLevel(memory.readKey(1), false);
		W = Math.min(getWidth() / Level.XLEN, getHeight() / Level.YLEN);
	}

	public void loadLevel(int nbr, boolean register) {
		nbr = Math.min(nbr, Data.Data.length-1);
		level = new Level(nbr);
		x = level.getPlayerX();
		y = level.getPlayerY();
		repaint();
		if(register)
			memory.saveKey(1, nbr);
		setTitle("Level " + (nbr+1) + " / " + Data.Data.length);
	}
	
	public void restartLevel() {
		loadLevel(level.nbr, false);
	}
	
	public void nextLevel() {
		loadLevel(level.nbr+1, true);
	}

	public void move(int dx, int dy) {
		if(x+dx >= 0 && x+dx < Level.XLEN && y+dy >= 0 && y+dy < Level.YLEN)
			if(level.hasTile(x+dx, y+dy)) {
				level.removeTile(x, y);
				x += dx;
				y += dy;

				if(level.remaining() == 1) {
					nextLevel();
					repaint();
					try { Thread.sleep(250); } catch (Exception ex) {}
				}
			}
	}
	
	public void keyPressed(int keyCode) {
		switch(getGameAction(keyCode)) {
			case Canvas.LEFT:  move(-1, 0); break;
			case Canvas.RIGHT: move(+1, 0); break;
			case Canvas.UP:    move(0, -1); break;
			case Canvas.DOWN:  move(0, +1); break;
		}
		repaint();
	}

	public void drawPlayer(Graphics g, int x, int y) {
		g.setColor(0, 0, 255);
		g.fillArc(x*W, y*W, W, W, 0, 360);
		g.setColor(0, 0, 100);
		g.drawArc(x*W, y*W, W, W, 0, 360);
	}

	public void drawTile(Graphics g, int x, int y) {
		g.setColor(255, 0, 0);
		g.fillArc(x*W, y*W, W, W, 0, 360);
		g.setColor(100, 0, 0);
		g.drawArc(x*W, y*W, W, W, 0, 360);
	}

	public void paint(Graphics g) {
		g.setColor(40, 40, 40);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(255, 0, 0);
		for(int y = 0; y < Level.YLEN; y++)
			for(int x = 0; x < Level.XLEN; x++)
				if(level.hasTile(x, y))
					drawTile(g, x, y);
		drawPlayer(g, x, y);
	}
}
