// Solitaire - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com> //

package game;

public class Deck
{
	private Card cards[];
	private int len = 0, visible = 0;  // when applicable

	public Deck(int capacity) {
		cards = new Card[capacity];
	}

	public Card get(int n) { return cards[len-n-1]; }
	public void add(Card card) { cards[len++] = card; }
	public void remove(int n, int many) {
		n = len - n - 1;
		for(int i = n; i < len - many; i++)
			cards[i] = cards[i+many];
		len -= many;
	}

	public int length() { return len; }

	public void setVisible(int n) { visible = (n > len) ? len : n; }
	public boolean isVisible(int n) { return n < visible; }
	public int getVisibleLength() { return visible; }
}
