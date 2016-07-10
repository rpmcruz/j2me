// Tetris - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;
import javax.microedition.lcdui.*;

public class MyCanvas extends Canvas implements Runnable//, CommandListener
{
	private boolean done = false;
	private Command down_cmd, restart_cmd = null;
	
	private MyData data;
	private MyDraw draw;

	MyCanvas() {
		super();
		setFullScreenMode(true);
/*		addCommand(new Command("Options", Command.BACK, 0));
		addCommand(down_cmd = new Command("Down", "Down", Command.OK, 1));
		setCommandListener(this);*/

		data = new MyData();
		data.nextType();
		draw = new MyDraw(data, getWidth(), getHeight());
		start();
	}
	
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		done = true;
	}

	public void paint(Graphics g) {
		g.setColor(0, 0, 0);
		g.fillRect(0, 0, getWidth(), getHeight());
		draw.paint(g, data);
		if(isGameover()) {
			draw.drawMessage(g, this, "Too bad", "You lost!", "", false, false);
			stop();
			removeCommand(down_cmd);
			down_cmd = null;
			addCommand(restart_cmd = new Command("Restart", "Restart", Command.OK, 1));
		}
	}
	
    public void run() {
		final int delay = 250 + ((9 - MySettings.get().speed)-5)*100;
		done = false;
        while (!done) {
			long t = System.currentTimeMillis();
			data.move(0, +1);
			repaint();
			long dt = System.currentTimeMillis() - t;
			if(delay > dt)
				try { Thread.sleep(delay - dt); } catch(Exception ex) {}
        }
    }

	public void keyPressed(int keyCode) {
		if(isGameover()) {
			if(getGameAction(keyCode) == FIRE)
				Tetris.get().showOptions(null);
			return;
		}
		
		switch(getGameAction(keyCode)) {
			case LEFT:  data.move(-1, 0); break;
			case RIGHT: data.move(+1, 0); break;
			case FIRE:  data.rotate(); break;
			case DOWN:  data.move(0, +1); break;
			case UP:    data.down(); break;
		}
		repaint();
	}
	
	private boolean isGameover() {
		return data.board.isGameover();
	}
	
/*	public void commandAction(Command cmd, Displayable d)  {
		MyCanvas game = isGameover() ? null : this;

		if(cmd == down_cmd) {
			data.down();
			repaint();
		}
		else if(cmd == restart_cmd)
			Tetris.get().restart();
		else {
			stop();
			Tetris.get().showOptions(game);
		}
	}*/
}
