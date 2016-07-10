// Minefield - (C) 2005-2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;

public class MySettings
{
	public static final int EASY = 1;
	public static final int MEDM = 2;
	public static final int HARD = 3;
	public static final int CUSTOM = 0;
	
	private static MySettings singleton = null;
	public int width, height, mines;  // read-only
	public int mode;
	private Memory highscore = null;

	public static MySettings get() {
		if(singleton == null)
			singleton = new MySettings();
		return singleton;
	}

	public MySettings() {
		setSize(EASY);
	}
	
	public void destroy() {
		if(highscore != null)
			highscore.close();
		singleton = null;
	}
	
	public final void setSize(int mode) {
		switch(mode) {
			case EASY: width = height = 8; mines = 10; break;
			case MEDM: width = height = 16; mines = 40; break;
			case HARD: width = 30; height = 16; mines = 99; break;
		}
		this.mode = mode;
	}

	public void setCustomSize(int width, int height, int mines) {
		this.width = width;
		this.height = height;
		this.mines = mines;
		mode = CUSTOM;
	}

	public int readScore(int mode) {
		if(mode == CUSTOM) return 9999;
		if(highscore == null)
			highscore = new Memory("MinefieldScores", 3);
		return highscore.readKey(mode);
	}

	public void saveScore(int time) {
		if(mode == CUSTOM) return;
		if(highscore == null)
			highscore = new Memory("MinefieldScores", 3);
/*		int cur_time = highscore.readKey(mode);
		if(cur_time > 0 && cur_time <= time)
			return false;*/
		highscore.saveKey(mode, time);
	}
}
