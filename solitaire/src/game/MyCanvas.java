// Solitaire - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com> //

package game;
import javax.microedition.lcdui.*;

public class MyCanvas extends Canvas implements CommandListener
{
	private MyData data;
	private MyDraw draw;
	private long fireTime = 0;

	private Command select_cmd, restart_cmd = null;

	public MyCanvas() {
		addCommand(new Command("Options", Command.BACK, 0));
		addCommand(select_cmd = new Command("Select", "Select", Command.OK, 1));
		setCommandListener(this);

		draw = new MyDraw();
		data = new MyData();
		if(getWidth() >= 240)
			draw.superSizeMe();
		setTitle("Solitaire");
    }

	public void paint(Graphics g)  {
		g.setColor(0, 255, 0);
		g.fillRect(0, 0, getWidth(), getHeight());

		draw.paint(g, this, data);
	}
	
	public void keyPressed(int keyCode) {
		if(data.isGameover()) return;

		switch(getGameAction(keyCode)) {
			case FIRE: select(); break;
			case LEFT:  move(-1, 0); break;
			case RIGHT: move(+1, 0); break;
			case UP:    move(0, -1); break;
			case DOWN:  move(0, +1); break;
		}
    }
		
	public void select() {
		if(System.currentTimeMillis() - fireTime < 750) {
			data.double_click();
			fireTime = 0;
			
			if(data.isGameover()) {
				removeCommand(select_cmd);
				select_cmd = null;
				addCommand(restart_cmd = new Command("Restart", "Restart", Command.OK, 1));
			}
		}
		else {
			if(data.select())
				fireTime = 0;
			else
				fireTime = System.currentTimeMillis();
		}

		repaint();
	}
	
	public void move(int dx, int dy) {
		if(dx != 0)
			data.moveX(dx);
		if(dy != 0)
			data.moveY(dy);
		fireTime = 0;
		repaint();
	}
	
	public void commandAction(Command cmd, Displayable d)  {
		MyCanvas game = data.isGameover() ? null : this;

		if(cmd == select_cmd)
			select();
		else if(cmd == restart_cmd)
			Solitaire.get().restart();
		else
			Solitaire.get().showOptions(game);
	}
}
