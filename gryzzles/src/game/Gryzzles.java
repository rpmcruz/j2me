// Gryzzles J2ME - (C) 2011-2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class Gryzzles extends MIDlet implements CommandListener
{
	private MyCanvas canvas;
	public Display display;
	
	public void startApp() {
	    display = Display.getDisplay(Gryzzles.this);

		canvas = new MyCanvas();
		canvas.setCommandListener(Gryzzles.this);
		canvas.addCommand(new Command("Options", Command.CANCEL, 0));
		canvas.addCommand(new Command("Restart", Command.OK, 1));
		display.setCurrent(canvas);
	}

	public void pauseApp() {
	}
	
	public void destroyApp(boolean unconditional) {
	}

	
	public void commandAction(Command cmd, Displayable disp) {
	    Display display = Display.getDisplay(Gryzzles.this);

		switch(cmd.getPriority()) {
			case 0:
				display.setCurrent(new OptionsForm());
				break;
			case 1:
				canvas.restartLevel();
				break;
		}
	}

	private class OptionsForm extends List implements CommandListener
	{
		OptionsForm() {
			super(canvas.getTitle(), Choice.IMPLICIT);
			append("Continue", null);
			append("Restart Level", null);
			append("Reset Game", null);
			append("Instructions", null);
			addCommand(new Command("Exit", Command.EXIT, 2));
			setCommandListener(OptionsForm.this);
		}

		public void commandAction(Command cmd, Displayable disp) {
			if(cmd.getPriority() == 2) {		// exit
				destroyApp(false);
				notifyDestroyed();
			}
			else {
				Displayable nextDisp = canvas;
				
				switch(getSelectedIndex()) {
					case 0: break;
					case 1:	canvas.restartLevel(); break;
					case 2:	canvas.loadLevel(0, true); break;
					// instructions
					case 3: nextDisp = new InstructionsForm(this); break;
				}

				display.setCurrent(nextDisp);
			}
		}
	}

	private class InstructionsForm extends Form implements CommandListener {
		private Displayable backDisp;
	  
        private static final String help =
			"You must find a way to eat up all red circles without getting stuck " +
			"in the midst of a vacuum of red tiles." +
			"\n\nby Ricardo Cruz <ric8cruz@gmail.com>";

		  InstructionsForm(Displayable backDisp) {
			super("Instructions");
			Image img = null;
			try { img = Image.createImage("/tile.png"); } catch(Exception ex) { }
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
