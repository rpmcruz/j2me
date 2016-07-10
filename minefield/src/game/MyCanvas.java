// Minefield - (C) 2005-2012 Ricardo Cruz <ric8cruz@gmail.com>

/*
 * MVC-model:
 * - MinesDisplay: controller
 * - MinesView: view
 * - MinesData: model
 * 
 * The controller takes care of modifying the model on changes, and
 * relaying drawing requests to the view.
 * 
 * The controller also implements the timer (for the status bar).
 */

package game;
import javax.microedition.lcdui.*;

public class MyCanvas extends Canvas implements CommandListener
{
	private MyData data;
	private MyDraw draw;
	private int x = 0, y = 0;
	private long fireTime = 0;
	private Command uncover_cmd, flag_cmd, restart_cmd = null;

	public MyCanvas() {
		addCommand(new Command("Options", Command.BACK, 0));
		addCommand(uncover_cmd = new Command("Uncover", "Uncover mine", Command.OK, 1));
		addCommand(flag_cmd = new Command("Flag", "Place flag", Command.ITEM, 2));
		setCommandListener(this);

		MySettings s = MySettings.get();
		draw = new MyDraw();
		data = new MyData(s.width, s.height, s.mines);
		setTitle("Mines: " + data.minesLeft());
		
		// initial hand: uncover some tiles
		for(int x = 0; x < s.width; x++)
			for(int y = 0; y < s.height; y++) {
				if(data.getTile(x,y) == 0) {
					data.uncover(x,y);
					this.x = x == 0 ? x : x-1;
					this.y = y == 0 ? y : y-1;
					return;  // all done & done
				}
			}
    }

	public void paint(Graphics g)  {
		draw.paint(g, this, data, x, y);
	}

	public void keyPressed(int keyCode) {
		switch(getGameAction(keyCode)) {
			case FIRE: uncover(); break;
			case LEFT:  move(-1, 0); break;
			case RIGHT: move(+1, 0); break;
			case UP:    move(0, -1); break;
			case DOWN:  move(0, +1); break;
			default:
				switch(keyCode) {
					case Canvas.KEY_NUM1: move(-1, -1); break;
					case Canvas.KEY_NUM2: move(+0, -1); break;
					case Canvas.KEY_NUM3: move(+1, -1); break;
					case Canvas.KEY_NUM4: move(-1, +0); break;
					case Canvas.KEY_NUM5: uncover(); break;
					case Canvas.KEY_NUM6: move(+1, +0); break;
					case Canvas.KEY_NUM7: move(-1, +1); break;
					case Canvas.KEY_NUM8: move(+0, +1); break;
					case Canvas.KEY_NUM9: move(+1, +1); break;
					case Canvas.KEY_STAR: flag(); break;
					case Canvas.KEY_POUND: uncover(); break;
				}
		}
    }

	public void uncover() {
		if(data.isGameover()) return;

		if(data.isUncovered(x,y)) {
			// double click on uncovered: uncover neighbors
			if(System.currentTimeMillis() - fireTime < 1000)
				data.uncoverNeighbors(x, y);
			fireTime = System.currentTimeMillis();
		}
		else
			data.uncover(x, y);
		repaint();

		if(data.isGameover()) {
			removeCommand(uncover_cmd);
			removeCommand(flag_cmd);
			uncover_cmd = flag_cmd = null;
			addCommand(restart_cmd = new Command("Restart", "Restart", Command.OK, 1));
		}
	}

	public void flag() {
		if(data.isGameover()) return;
		
		data.flag(x, y);
		repaint();
		setTitle("Mines: " + data.minesLeft());
	}
	
	public void move(int dx, int dy) {
		MySettings s = MySettings.get();
		if(x+dx >= 0 && x+dx < s.width)
			x += dx;
		if(y+dy >= 0 && y+dy < s.height)
			y += dy;
		repaint();
		fireTime = 0;
	}

	public void commandAction(Command cmd, Displayable d)  {
		MyCanvas game = data.isGameover() ? null : this;
		
		if(cmd == uncover_cmd)
			uncover();
		else if(cmd == flag_cmd)
			flag();
		else if(cmd == restart_cmd)
			Minefield.get().restart();
		else
			Minefield.get().showOptions(game);
	}
}
