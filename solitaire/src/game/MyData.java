// Solitaire - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com> //

package game;
import java.util.Random;

public class MyData
{
	static public final int CLUBS = 0, SPADES = 1, HEARTS = 2, DIAMONDS = 3;

	public Deck decks[], goals[], deck;
	public Deck cursor, selected, old_cursor = null;
	public boolean cursor_deck1 = false;  // HACK
	private boolean gameover = false;
	
	public MyData() {
		Random gen = new Random();
		gen.setSeed(System.currentTimeMillis());

		// shuffle cards
		Card cards[] = new Card[13*4];
		for(int i = 0; i < cards.length; i++)
			cards[i] = new Card(i%13, i/13);
		for(int i = 0; i < cards.length; i++) {
			int j = gen.nextInt(13*4);
			Card t = cards[i];
			cards[i] = cards[j];
			cards[j] = t;
		}
		
		int n = 0;
		decks = new Deck[7];
		for(int i = 0; i < 7; i++) {
			decks[i] = new Deck(13 + i);
			for(int j = 0; j < i+1; j++)
				decks[i].add(cards[n+j]);
			n += i+1;
			decks[i].setVisible(1);
		}
		
		deck = new Deck(cards.length - n);
		for(int i = n; i < cards.length; i++)
			deck.add(cards[i]);

		goals = new Deck[4];
		for(int i = 0; i < 4; i++)
			goals[i] = new Deck(13);

		cursor = decks[0];
		selected = null;
	}
	
	private boolean calcGameover() {
		int sum = 0;
		for(int i = 0; i < goals.length; i++)
			sum += goals[i].length();
		return sum == 52;
	}
	
	public boolean isGameover() {
		return gameover;
	}
	
	public boolean select() {
		if(cursor == deck && cursor_deck1) {
			giveCardDeck();
			cursor_deck1 = false;
			return true;
		}
		if(selected == cursor)
			selected = null;
		else if(cursor.getVisibleLength() == 0 && cursor.length() > 0 && cursor != deck) {
			cursor.setVisible(1);
			selected = null;
			return true;
		}
		else if(selected != null && tryMove(selected, cursor)) {
			selected = null;
			return true;
		}
		else if(cursor.length() == 0) {
			selected = null;
			return true;
		}
		else
			selected = cursor;
		return false;
	}
	
	public void double_click() {
		if(cursor == deck && cursor_deck1) return;
		int _c = (cursor == deck) ? deck.getVisibleLength()-1 : 0;
		Card c = cursor.get(_c);
		
		int s = c.suit();
		int n = c.number();
		if(n == 0 || (goals[s].length() > 0 && n-1 == goals[s].get(0).number())) {
			moveCards(cursor, goals[s], _c, 1);
			gameover = calcGameover();
		}
		selected = null;
	}
	
	public void moveX(int dx) {
		if(cursor == deck) {
			if(cursor_deck1 && dx < 0) {
				giveCardDeck();
				cursor_deck1 = false;
			}
			else
				cursor_deck1 = dx < 0;
		}
		else
			for(int d = 0; d < decks.length; d++)
				if(decks[d] == cursor) {
					d += dx;
					if(d < 0) d = decks.length-1;
					if(d >= decks.length) d = 0;
					cursor = decks[d];
					break;
				}
	}

	public void moveY(int dy) {
		if(cursor == deck)
			cursor = old_cursor;
		else {
			old_cursor = cursor;
			cursor = deck;
			cursor_deck1 = false;
		}
	}

	public void giveCardDeck() {
		int v = deck.getVisibleLength();
		int l = deck.length();
		deck.setVisible(v+1 > l ? 0 : v+1);
		selected = null;
	}

	public boolean tryMove(Deck src, Deck dst) {
		if(dst == deck || (dst.length() > 0 && dst.getVisibleLength() == 0))
			return false;

		int n, amount = 0;
		if(src == deck) {
			n = src.getVisibleLength()-1;
			amount = 1;
		}
		else {
			n = 0;
			if(dst.length() == 0)
				amount = src.getVisibleLength();
			else {
				int _amount = dst.get(0).number() - src.get(0).number();
				if(_amount > 0 && _amount <= src.getVisibleLength())
					amount = _amount;
			}
		}

		if(amount > 0) {
			Card c = src.get(n+amount-1);
			if(dst.length() == 0) {
				if(c.number() != 12 /*king*/)
					amount = 0;
			}
			else {
				Card c2 = dst.get(0);
				if(c.number()+1 != c2.number() || c.suit()/2 == c2.suit()/2)
					amount = 0;
			}
			if(amount > 0) {
				moveCards(src, dst, n, amount);
				return true;
			}
		}

		return false;
	}
	
	public void moveCards(Deck src, Deck dst, int n, int amount) {
		for(int i = n+amount-1; i >= n; i--)
			dst.add(src.get(i));
		src.remove(n, amount);
		src.setVisible(src.getVisibleLength() - amount);
		dst.setVisible(dst.getVisibleLength() + amount);
	}
}
