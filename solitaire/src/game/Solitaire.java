// Solitaire - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class Solitaire extends MIDlet
{
	private static Solitaire singleton;
	public Display display;

	public Solitaire() {
		display = Display.getDisplay(this);
		showOptions(null);
		singleton = this;
	}

	static public Solitaire get() {
		return singleton;
	}
	
	public void startApp() throws MIDletStateChangeException {}

	public void pauseApp() {}
  
	public void destroyApp(boolean unconditional) {}

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
			super("Solitaire", Choice.IMPLICIT);
			if(backDisp != null)
				append("Continue", null);
			append("New Game", null);
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
				case 0: break;
				// new game
				case 1: nextDisp = new MyCanvas(); break;
				// instructions
				case 2: nextDisp = new InstructionsForm(this); break;
			}
			display.setCurrent(nextDisp);
		}
	}

	private class InstructionsForm extends Form implements CommandListener {
		private Displayable backDisp;
	  
        private static final String help = "Solitaire. Use arrow keys to move around the "
				+ "decks. Press once to select cards and again to drop them. Double-click "
				+ "to send them to the ending piles."
				+ "\n\nby Ricardo Cruz <ric8cruz@gmail.com>";

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
