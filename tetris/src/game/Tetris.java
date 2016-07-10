// Tetris - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class Tetris extends MIDlet
{
	private static Tetris singleton;
	public Display display;

	public Tetris() {
		display = Display.getDisplay(this);
		showOptions(null);
		singleton = this;
	}

	static public Tetris get() {
		return singleton;
	}
	
	public void startApp() throws MIDletStateChangeException {
    }
  
	public void pauseApp() {}
  
	public void destroyApp(boolean unconditional) {
	}

	public void showOptions(MyCanvas game) {
		display.setCurrent(new OptionsForm(game));
	}
	
	public void restart() {
		display.setCurrent(new MyCanvas());
	}
	
	private class OptionsForm extends List implements CommandListener
	{
		private Displayable backDisp;

		OptionsForm(Displayable backDisp) {
			super("Tetris", Choice.IMPLICIT);
			if(backDisp != null)
				append("Continue", null);
			append("New Game", null);
			append("Set Speed", null);
			append("Instructions", null);
			addCommand(new Command("Exit", Command.EXIT, 2));
			setCommandListener(OptionsForm.this);
			this.backDisp = backDisp;
		}

		public void commandAction(Command cmd, Displayable disp) {
			if(cmd.getPriority() == 2) {
				// exit
				destroyApp(false);
				notifyDestroyed();
				return;
			}
			Displayable nextDisp = backDisp;
			int i = getSelectedIndex() + (backDisp == null ? +1 : 0);
			switch(i) {
				// continue
				case 0: ((MyCanvas) nextDisp).start(); break;
				// new game
				case 1: nextDisp = new MyCanvas(); break;
				// speed
				case 2:
					nextDisp = new SpeedForm(this);
					if(backDisp != null) {
						// don't show "Continue"
						delete(0);
						backDisp = null;
					}
					break;
				// instructions
				case 3: nextDisp = new InstructionsForm(this); break;
			}
			display.setCurrent(nextDisp);
		}
	}

 	private class SpeedForm extends Form implements CommandListener {
		private Displayable backDisp;
		private Gauge gauge;

		SpeedForm(Displayable backDisp) {
			super("Speed");
			append(gauge = new Gauge("Game speed:", true, 9, MySettings.get().speed));
			addCommand(new Command("Save", Command.BACK, 0));
			setCommandListener(SpeedForm.this);
			this.backDisp = backDisp;
		}

		public void commandAction(Command cmd, Displayable disp) {
			Displayable nextDisp = backDisp;

			MySettings.get().speed = gauge.getValue();

			display.setCurrent(nextDisp);
		}
	}

	private class InstructionsForm extends Form implements CommandListener {
		private Displayable backDisp;
	  
        private static final String help =
			"Yet another Tetris clone." +
			"\n\nby Ricardo Cruz <ric8cruz@gmail.com>";

		  InstructionsForm(Displayable backDisp) {
			super("Instructions");
			Image img = null;
			try { img = Image.createImage("/icon.png"); } catch(Exception ex) { }
			if(img != null)
				append(img);
			append(help);
			addCommand(new Command("Ok", Command.BACK, 0));
			setCommandListener(InstructionsForm.this);
			this.backDisp = backDisp;
		}

		public void commandAction(Command c, Displayable d) {
			display.setCurrent(backDisp);
		}
	}
}
