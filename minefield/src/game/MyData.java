// Minefield - (C) 2005-2012 Ricardo Cruz <ric8cruz@gmail.com>

/*
 * MVC-model:
 * - MinesDisplay: controller
 * - MinesView: view
 * - MinesData: model
 */

package game;
import java.util.Random;

public class MyData
{
	static private final byte MINE = 9, DIGITS = 15;
	static private final byte UNCOVERED = 16, FLAG = 32;
	private byte field[][];

	private int uncovered, flags, mines;
	private int w, h, area;

	static public final byte PLAY = 0, LOST = 1, WON = 2;
	public byte status = PLAY;  // read-only

	private long startTime = System.currentTimeMillis(), endTime = 0;
	public int prevHighscore = 0;

	public MyData(int w, int h, int mines) {
		this.w = w;
		this.h = h;
		this.mines = mines;
		area = w * h;

		field = new byte[w][h];
		generate();
	}

	private void generate() {
		uncovered = flags = 0;
		int x,y,i;

		// clean the field array
		for(x = 0; x < w; x++)
			for(y = 0; y < h; y++)
				field[x][y] = 0;

		Random generator = new Random();
		generator.setSeed(System.currentTimeMillis());

		// let's place the mines
		for(i = 0; i < mines;) {
			int j = generator.nextInt(area);
			x = j / h;
			y = j % h;

			if(field[x][y] == MINE)
				continue;  // already got a mine, try again
			field[x][y] = MINE;
			i++;
		}

		// let's indicate the neighbor mines
		for(x = 0; x < w; x++)
			for(y = 0; y < h; y++) {
				if(hasMine(x, y))
					continue;
				i = 0;
				for(int dx = -1; dx <= 1; dx++)
					for(int dy = -1; dy <= 1; dy++)
						if(hasMine(x+dx, y+dy)) i++;
				field[x][y] = (byte) i;
			}
    }

	public boolean hasMine(int x, int y) {
		if(x < 0 || x >= w || y < 0 || y >= h)
			return false;
		return (field[x][y] & DIGITS) == MINE;
	}

	public int getTile(int x, int y) {
		return field[x][y] & DIGITS;
	}

	public boolean isUncovered(int x, int y) {
		return (field[x][y] & UNCOVERED) != 0;
	}
	
	public boolean isFlagged(int x, int y) {
		return (field[x][y] & FLAG) != 0;
	}
	
	public void uncover(int x, int y) {
		if(x < 0 || x >= w || y < 0 || y >= h)
			return;
		if((field[x][y] & (FLAG|UNCOVERED)) == 0) {
			field[x][y] |= UNCOVERED;
			uncovered++;

			if((field[x][y] & DIGITS) == 0)
				// empty tile; uncover neighbors
				// hack: there will be unnecessary calls
				uncoverNeighbors(x, y);
			else if((field[x][y] & DIGITS) == MINE)
				status = LOST;
			if(uncovered == area - mines)
				status = WON;

			if(status != PLAY) {
				endTime = System.currentTimeMillis();
				if(status == WON) {
					MySettings s = MySettings.get();
					prevHighscore = MySettings.get().readScore(s.mode);
					if(newHighscore())
						s.saveScore(timeElapsed());
				}
			}
		}
	}

	public void uncoverNeighbors(int x, int y) {
		for(int dx = -1; dx <= 1; dx++)
			for(int dy = -1; dy <= 1; dy++)
				uncover(x+dx, y+dy);
	}
	
	public void flag(int x, int y) {
		if((field[x][y] & UNCOVERED) != 0)
			return;
		if((field[x][y] & FLAG) != 0) {
			// unflag if set
			field[x][y] -= FLAG;
			flags--;
		}
		else {
			field[x][y] |= FLAG;
			flags++;
		}
	}
	
	public boolean isGameover() {
		return status != PLAY;
	}
	
	public int timeElapsed() {
		return (int)((endTime - startTime) / 1000);
	}
	
	public int minesLeft() {
		return mines - flags;
	}
	
	public boolean newHighscore() {
		return status == WON && (prevHighscore == 0 || prevHighscore >= timeElapsed());
	}
}
