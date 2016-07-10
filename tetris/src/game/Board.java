// Tetris - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;

public class Board
{
	public static final int WIDTH = 10, HEIGHT = 20;

	private byte board[][];
	private boolean gameover = false;  // cache
	
	public Board() {
		board = new byte[HEIGHT][WIDTH];
		for(int y = 0; y < HEIGHT; y++)
			for(int x = 0; x < WIDTH; x++)
				board[y][x] = 0;
	}
	
	public byte get(int x, int y) {
		if(x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT)
			return board[y][x];
		return 0;
	}
	
	public void set(int x, int y, byte color) {
		if(x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT)
			board[y][x] = color;
		gameover = calcGameover();
	}

	public void cutLine(int y) {
		for(int _y = y; _y > 0; _y--)
			for(int x = 0; x < Board.WIDTH; x++)
				board[_y][x] = board[_y-1][x];
		for(int x = 0; x < WIDTH; x++)
			board[0][x] = 0;
	}

	public boolean lineFull(int y) {
		for(int x = 0; x < WIDTH; x++)
			if(board[y][x] == 0)
				return false;
		return true;
	}
	
	public boolean isGameover() { return gameover; }
	
	public boolean calcGameover() {
		for(int x = 0; x < WIDTH; x++)
			if(board[0][x] != 0)
				return true;
		return false;
	}
}
