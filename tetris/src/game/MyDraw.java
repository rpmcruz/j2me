// Tetris - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;
import javax.microedition.lcdui.*;

public class MyDraw
{
	public int BLOCK_W;
	
	private Font msg = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
	private Image smile;

	public MyDraw(MyData data, int dispWidth, int dispHeight) {
		try { smile = Image.createImage("/smile.png"); }
		catch(Exception ex) { System.out.println("couldn't load smile"); }

		int bw = (dispWidth-6)/(Board.WIDTH+4), bh = dispHeight/Board.HEIGHT;
		BLOCK_W = bw < bh ? bw : bh;
	}
	
	private void setBlockColor(Graphics g, int type) {
		switch(type) {
			case 1: g.setColor(0,255,255); break;		// I
			case 2: g.setColor(0,0,255); break;			// J
			case 3: g.setColor(255,128,0); break;		// L
			case 4: g.setColor(255,255,0); break;		// O
			case 5: g.setColor(0,255,0); break;			// S
			case 6: g.setColor(128,0,255); break;		// T
			case 7: g.setColor(255,0,0); break;			// Z
		}
	}

	public void drawSquare(Graphics g, int color, int x, int y) {
		setBlockColor(g, color);
		g.fillRect(x*BLOCK_W, y*BLOCK_W, BLOCK_W, BLOCK_W);
		g.setColor(255, 255, 255);
		g.drawRect(x*BLOCK_W, y*BLOCK_W, BLOCK_W, BLOCK_W);
	}
	
	public void drawBoard(Graphics g, Board board) {
		g.setColor(255, 255, 255);
		g.drawRect(0, 0, Board.WIDTH*BLOCK_W, Board.HEIGHT*BLOCK_W - 1);
		
		for(int y = 0; y < Board.HEIGHT; y++)
			for(int x = 0; x < Board.WIDTH; x++) {
				byte type = board.get(x, y);
				if(type > 0)
					drawSquare(g, type, x, y);
			}
	}

	public void drawBlock(Graphics g, Block block, int x, int y) {
		for(int _y = 0; _y < 4; _y++)
			for(int _x = 0; _x < 4; _x++)
				if(block.hasInv(_x, _y))
					drawSquare(g, block.type+1, x+_x, y-_y);
	}

	public void drawNext(Graphics g, Block next) {
		g.setColor(255, 255, 255);
		g.drawRect(11*BLOCK_W - 2, 0, BLOCK_W*4+3, BLOCK_W*4+3);
		drawBlock(g, next, 11, 3);
	}
	
	public void paint(Graphics g, MyData data) {
		drawBoard(g, data.board);
		drawBlock(g, data.block, data.bx, data.by);
		drawNext(g, data.next);
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

		g.drawRegion(smile, happy ? 0 : 24, 0, 24, 24, 0, _x+20-12, _y+20-12, 0);
	}
}
