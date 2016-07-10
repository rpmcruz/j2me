// Tetris - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;

public class Block
{
	public static final int TYPES = 7;
	public int type, variant, model;

	public Block(int type, int variant) {
		this.type = type;
		this.variant = variant;
		this.model = getModel(type, variant);
	}

	int getModel(int type, int variant) {
		// variantions in clock-wise terms
		switch(type) {
			case 0:  // I
				switch(variant%2) {
					case 0: return (2<<0)|(2<<4)|(2<<8)|(2<<12);
					case 1: return (0<<0)|(0<<4)|(0<<8)|((1|2|4|8)<<12);
				}
			case 1:  // J
				switch(variant) {
					case 0: return (0<<0)|(4<<4)|(4<<8)|((2|4)<<12);
					case 1: return (0<<0)|(0<<4)|(1<<8)|((1|2|4)<<12);
					case 2: return (0<<0)|((1|2)<<4)|(1<<8)|(1<<12);
					case 3: return (0<<0)|((1|2|4)<<4)|(4<<8)|(0<<12);
				}
			case 2:  // L
				switch(variant) {
					case 0: return (0<<0)|(1<<4)|(1<<8)|((1|2)<<12);
					case 1: return (0<<0)|((1|2|4)<<4)|(1<<8)|(0<<12);
					case 2: return (0<<0)|((2|4)<<4)|(4<<8)|(4<<12);
					case 3: return (0<<0)|(0<<4)|(4<<8)|((1|2|4)<<12);
				}
			case 3:  // O
				return (0<<0)|((1|2)<<4)|((1|2)<<8)|(0<<12);
			case 4:  // S
				switch(variant%2) {
					case 0: return (0<<0)|(0<<4)|((2|4)<<8)|((1|2)<<12);
					case 1: return (0<<0)|(1<<4)|((1|2)<<8)|(2<<12);
				}
			case 5:  // T
				switch(variant) {
					case 0: return (0<<0)|(0<<4)|(2<<8)|((1|2|4)<<12);
					case 1: return (0<<0)|(1<<4)|((1|2)<<8)|(1<<12);
					case 2: return (0<<0)|((1|2|4)<<4)|(2<<8)|(0<<12);
					case 3: return (0<<0)|(4<<4)|((2|4)<<8)|(4<<12);
				}
			case 6:  // Z
				switch(variant%2) {
					case 0: return (0<<0)|(0<<4)|((1|2)<<8)|((2|4)<<12);
					case 1: return (0<<0)|(2<<4)|((1|2)<<8)|(1<<12);
				}
		}
		return 0;  // error
	}

	public Block rotate() {
		return new Block(type, (variant+1)%4);
	}
	
	public int getLine(int y) {
		return (model >> (y*4)) & 15;
	}
	
	// public reads block as a 4x4 matrix
	public boolean has(int x, int y) {
		return (getLine(y) & (1 << x)) != 0;
	}
	
	// inverted 4x4 (using math orthodox referentials)
	public boolean hasInv(int x, int y) {
		return has(x, 3-y);
	}

	public int offsetX() {
		for(int x = 0; x < 2; x++)
			for(int y = 0; y < 4; y++)
				if(has(x, y))
					return x;
		return 2;
	}

	public int width() {
		int w = 0;
		for(int x = 0; x < 4; x++)
			for(int y = 0; y < 4; y++)
				if(has(x,y)) {
					w++;
					break;
				}
		return w;
	}

	public int offsetY() {
		for(int y = 3; y > 1; y--)
			if(getLine(y) != 0)
				return 3-y;
		return 1;
	}
}
