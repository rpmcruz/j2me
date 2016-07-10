// Solitaire - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;

public class MyDraw
{
	private int CARD_WIDTH = 16, CARD_HEIGHT = 25, CURSOR_SPACE = 15, SS = 7, XPAD = 2;
	private int CARD_OFFSET = 10, BACK_OFFSET = 3, CARD_YPAD = 6, YPAD = 2;
	private Image suits, font, hand;

	private Font msg = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
	private Image smile;

	public MyDraw() {
		try { smile = Image.createImage("/smile.png"); } catch(Exception ex) {}

		try {
			suits = Image.createImage("/suits.png");
			font = Image.createImage("/font.png");
			hand = Image.createImage("/hand.png");
		} catch(Exception ex) {}
	}

	private static Image scale2x(Image src) {
		int w = src.getWidth(), h = src.getHeight();
		
		int rgb[] = new int[w*h];
		src.getRGB(rgb, 0, w, 0, 0, w, h);

		int yo = w*2, yo4 = w*4;
		
		int rgbData[] = new int[w * h * 4];
		for(int x = 0; x < w; x++)
			for(int y = 0; y < h; y++) {
				int c = rgb[x + y*w];
				rgbData[x*2 + y*yo4] = c;
				rgbData[(x*2)+1 + y*yo4] = c;
				rgbData[x*2 + (y*yo4)+yo] = c;
				rgbData[(x*2)+1 + (y*yo4)+yo] = c;
			}
		return Image.createRGBImage(rgbData, w*2, h*2, true);
	}

	public void superSizeMe() {
		suits = scale2x(suits);
		font = scale2x(font);
		hand = scale2x(hand);
		SS *= 2;
		CARD_WIDTH *= 2;
		CARD_HEIGHT *= 2;
		CARD_OFFSET *= 2;
		BACK_OFFSET *= 2;
		CARD_YPAD *= 2;
		CURSOR_SPACE *= 2;
		YPAD = 3;
	}

	public void paintSuit(Graphics g, int suit, int x, int y) {
		g.drawRegion(suits, suit*SS, 0, SS, SS, Sprite.TRANS_NONE, x, y, 0);
	}

	public void paintNumber(Graphics g, int n, int s, int x, int y) {
		g.drawRegion(font, n*SS, (s/2)*SS, SS, SS, Sprite.TRANS_NONE, x, y, 0);
	}

	public void paintCard(Graphics g, Card card, int x, int y, boolean selected) {
		int s = card.suit();
		int n = card.number();

		if(selected)
			g.setColor(250, 240, 0);
		else
			g.setColor(240, 240, 240);
		g.fillRect(x, y, CARD_WIDTH, CARD_HEIGHT);
		g.setColor(0,0,0);
		g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);

		paintSuit(g, s, x+(SS+XPAD-1), y+YPAD);
		paintNumber(g, n, s, x+(XPAD-1), y+YPAD);
		
		paintSuit(g, s, x+XPAD, y+CARD_HEIGHT-(SS+YPAD));
		paintNumber(g, n, s, x + SS + XPAD, y+CARD_HEIGHT-(SS+YPAD));
	}

	public void paintBackCard(Graphics g, int x, int y) {
		g.setColor(255, 150, 245);
		g.fillRect(x, y, CARD_WIDTH, CARD_HEIGHT);
		g.setColor(110, 65, 110);
		g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
	}

	public void paintEmpty(Graphics g, int x, int y) {
		g.setColor(0, 210, 0);
		g.fillRect(x, y, CARD_WIDTH, CARD_HEIGHT);
		g.setColor(0, 97, 0);
		g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
	}

	public void paintCursor(Graphics g, int type, int x, int y) {
		g.drawImage(hand, x, y, 0);
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
	
	void paint(Graphics g, Displayable disp, MyData data) {
		// user decks
		for(int i = 0; i < data.decks.length; i++) {
			int x = i*(CARD_WIDTH+XPAD) + XPAD, y = CARD_HEIGHT + CARD_YPAD, _y = 0;
			Deck d = data.decks[i];
			int deck_height =
				CARD_OFFSET * d.getVisibleLength() +
				BACK_OFFSET * (d.length() - d.getVisibleLength());
			boolean minimize = deck_height + y > disp.getHeight();
			for(int j = 0; j < d.length(); j++) {
				int n = d.length() - j - 1;
				Card c = d.get(n);

				if(d.isVisible(n))
					paintCard(g, c, x, y + _y, data.selected == d);
				else
					paintBackCard(g, x, y + _y);
				if(j+1 < d.length()) {
					if(d.isVisible(n)) {
						final int first = d.length() - d.getVisibleLength();
						final boolean shrink = d.isVisible(n) && (minimize && j != first);						
						_y += shrink ? CARD_OFFSET/2 : CARD_OFFSET;
					}
					else
						_y += BACK_OFFSET;
				}
			}
			if(d.length() == 0)
				paintEmpty(g, x, y);
			if(data.cursor == d)
				paintCursor(g, 0, x + XPAD*2, y + _y + CURSOR_SPACE);
		}

		// goals
		for(int i = 0; i < 4; i++) {
			int x0 = 3 * (CARD_WIDTH+XPAD) + XPAD;
			int x = i*(CARD_WIDTH+XPAD) + x0, y = YPAD;
			Deck d = data.goals[i];
			if(d.length() == 0)
				paintEmpty(g, x, y);
			else
				paintCard(g, d.get(0), x, y, false);
		}

		// deck (pool)
		if(data.deck.getVisibleLength() == data.deck.length())
			paintEmpty(g, XPAD, YPAD);
		else
			paintBackCard(g, XPAD, YPAD);
		if(data.deck.getVisibleLength() > 0) {
			Card c = data.deck.get(data.deck.getVisibleLength()-1);
			paintCard(g, c, XPAD + (CARD_WIDTH+XPAD), YPAD, data.selected == data.deck);
		}
		else
			paintEmpty(g, XPAD + (CARD_WIDTH+XPAD), YPAD);

		if(data.cursor == data.deck) {
			int in_deck = data.cursor_deck1 ? 0 : 1;
			paintCursor(g, 0, XPAD + (CARD_WIDTH+XPAD)*in_deck + XPAD*2, YPAD + CURSOR_SPACE);
		}
		
		if(data.isGameover())
			drawMessage(g, disp, "Victory !", "", "", true, false);
	}
}
