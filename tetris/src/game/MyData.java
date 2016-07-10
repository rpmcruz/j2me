// Tetris - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;
import java.util.Random;

public class MyData
{
	private Random gen;	

	public Board board;
	public Block block, next;
	public int bx, by;
	
	public MyData() {
		board = new Board();

		gen = new Random();
		gen.setSeed(System.currentTimeMillis());
		next = next();
	}

	private Block next() {
		int type = gen.nextInt(Block.TYPES);
		int variant = gen.nextInt(4);
		return new Block(type, variant);
	}
	
	public void nextType() {
		block = next;
		next = next();
		bx = Board.WIDTH/2;
		by = 0;
	}
	
	public void move(int dx, int dy) {
		if(touches(block, bx+dx, by+dy)) {
			if(dy > 0) {
				engrave(block, bx, by);
				testLines();
				nextType();
			}
		}
		else {
			bx += dx;
			by += dy;
		}
	}

	public boolean rotate() {
		Block rot = block.rotate();
		if(!touches(rot, bx, by))
			block = rot;
		return block == rot;
	}
	
	public void down() {
		while(!touches(block, bx, by+1))
			by++;
	}
	
	public boolean touches(Block block, int x, int y) {
		if(x+block.offsetX() < 0 || x+block.offsetX()+block.width() > Board.WIDTH || y-block.offsetY() >= Board.HEIGHT)
			return true;

		for(int _y = 0; _y < 4; _y++)
			for(int _x = 0; _x < 4; _x++)
				if(block.hasInv(_x, _y))
					if(board.get(x+_x, y-_y) != 0)
						return true;
		return false;
	}

	public void engrave(Block block, int x, int y) {
		for(int _y = 0; _y < 4; _y++)
			for(int _x = 0; _x < 4; _x++)
				if(block.hasInv(_x, _y))
					board.set(x+_x, y-_y, (byte)(block.type+1));
	}

	public void testLines() {
		for(int y = 0; y < Board.HEIGHT; y++)
			if(board.lineFull(y))
				board.cutLine(y);
	}
}
