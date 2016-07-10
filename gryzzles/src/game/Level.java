// Gryzzles J2ME - (C) 2011-2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;

public class Level
{
	// unlike Data, we reverse here the X by the Y because
	// displays are smaller horizontally
	public static final int XLEN = 16, YLEN = 10;
	
	public int nbr;
	private int data[];

	public Level(int level) {
		nbr = level;
		data = new int[Data.ROWS];
		System.arraycopy(Data.Data[nbr], 0, data, 0, Data.ROWS);
	}

	public boolean hasTile(int x, int y) {
		return (data[y] & (1 << x)) > 0;
	}
	
	public void removeTile(int x, int y) {
		if(hasTile(x, y))
			data[y] -= 1 << x;
	}
	
	public int getPlayerX() { return Data.DataPos[nbr][0]-1; }
	public int getPlayerY() { return Data.DataPos[nbr][1]-1; }
	
	public int remaining() {
		int sum = 0;
		for(int y = 0; y < YLEN; y++)
			for(int x = 0; x < XLEN; x++)
				sum += hasTile(x, y) ? 1 : 0;
		return sum;
	}
}
