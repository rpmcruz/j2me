// Minefield - (C) 2005-2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class Minefield extends MIDlet
{
	private static Minefield singleton;
	public Display display;

	public Minefield() {
		display = Display.getDisplay(this);
		showOptions(null);
		singleton = this;
	}

	static public Minefield get() {
		return singleton;
	}
	
	public void startApp() throws MIDletStateChangeException {
		MySettings.get();
    }
  
	public void pauseApp() {}
  
	public void destroyApp(boolean unconditional) {
		MySettings.get().destroy();
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
			super("Minefield", Choice.IMPLICIT);
			if(backDisp != null)
				append("Continue", null);
			append("New Game", null);
			append("Set Board Size", null);
			append("Highscore", null);
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
				// board size
				case 2:
					nextDisp = new BoardSizeForm(this);
					if(backDisp != null) {
						// don't show "Continue"
						delete(0);
						backDisp = null;
					}
					break;
				// highscore
				case 3: nextDisp = new HighscoreForm(this); break;
				// instructions
				case 4: nextDisp = new InstructionsForm(this); break;
			}
			display.setCurrent(nextDisp);
		}
	}

 	private class BoardSizeForm extends Form implements CommandListener {
		private Displayable backDisp;
		private ChoiceGroup choice;

		BoardSizeForm(Displayable backDisp) {
			super("Board Size");
			String[] choices = { "Novice", "Intermediate", "Expert", "Custom" };
			append(choice = new ChoiceGroup("Choose a size:", Choice.EXCLUSIVE, choices, null));
			int mode = MySettings.get().mode == MySettings.CUSTOM ? 3 : MySettings.get().mode-1;
			choice.setSelectedIndex(mode, true);
			addCommand(new Command("Save", Command.BACK, 0));
			setCommandListener(BoardSizeForm.this);
			this.backDisp = backDisp;
		}

		public void commandAction(Command cmd, Displayable disp) {
			Displayable nextDisp = backDisp;

			int mode = choice.getSelectedIndex();
			if(mode == 3)
				nextDisp = new CustomSizeForm(nextDisp);
			else
				MySettings.get().setSize(mode+1);

			display.setCurrent(nextDisp);
		}
	}

 	private class CustomSizeForm extends Form implements CommandListener {
		private Displayable backDisp;
		private TextField width, height, mines;

		CustomSizeForm(Displayable backDisp) {
			super("Choose a Size");
            append(width = new TextField("Width: ",  "8",  2, TextField.NUMERIC));
            append(height = new TextField("Height: ", "8",  2, TextField.NUMERIC));
            append(mines = new TextField("Mines: ",  "10", 2, TextField.NUMERIC));
			addCommand(new Command("Ok", Command.BACK, 0));
            setCommandListener(CustomSizeForm.this);
			this.backDisp = backDisp;
		}

		public void commandAction(Command cmd, Displayable disp) {
			int w = Integer.parseInt(width.getString());
			int h = Integer.parseInt(height.getString());
			int m = Integer.parseInt(mines.getString());
			MySettings.get().setCustomSize(w, h, m);
			display.setCurrent(backDisp);
		}
	}

 	private class HighscoreForm extends Form implements CommandListener {
		private Displayable backDisp;

		HighscoreForm(Displayable backDisp) {
			super("Highscore");
			MySettings s = MySettings.get();
			append("Novice: " + MyDraw.formatTime(s.readScore(MySettings.EASY)) + "\n");
			append("Intermediate: " + MyDraw.formatTime(s.readScore(MySettings.MEDM)) + "\n");
			append("Expert: " + MyDraw.formatTime(s.readScore(MySettings.HARD)));
			addCommand(new Command("Ok", Command.BACK, 0));
			setCommandListener(HighscoreForm.this);
			this.backDisp = backDisp;
		}

		public void commandAction(Command c, Displayable d) {
			display.setCurrent(backDisp);
		}
	}

	private class InstructionsForm extends Form implements CommandListener {
		private Displayable backDisp;
	  
        private static final String help = "Minefield, clone of the popular Minesweeper game.\n" +
                    "The goal is to uncover the entire field without detonating a landmine.\n" +
                    "Each tile has a number associated that mean the number of mines on its surround. " +
                    "It is using those numbers that you start identifying the location of the mines.\n" +
                    "You may place flags to help you identifying mines location.\n\n" +
                    "- Keys:\n" +
                    " 4,2,6,8 for left, up, right and down, respectively.\n" +
                    " # to uncover terrain.\n" +
                    " * to place a flag." +
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
