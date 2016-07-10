// Solitaire - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com> //

package game;

public class Card
{
	private int value;

	public Card(int number, int suit) {
		value = number + (suit<<4);
	}

	public int number() { return value & 15; }
	public int suit() { return (value >> 4) & 3; }
}
