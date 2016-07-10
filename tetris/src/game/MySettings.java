// Tetris - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com>

package game;

public class MySettings
{
	public int speed = 4;
	
	private static MySettings singleton = null;

	public static MySettings get() {
		if(singleton == null)
			singleton = new MySettings();
		return singleton;
	}
}
