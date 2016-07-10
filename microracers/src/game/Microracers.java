/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import libs.MyMath;

/**
 * @author rick2
 */
public class Microracers extends MIDlet
{
	private MyCanvas canvas;
	
	public void startApp() {
		canvas = new MyCanvas();
		Display disp = Display.getDisplay(this);
		disp.setCurrent(canvas);
		canvas.start();
	}
	
	public void pauseApp() {
	}
	
	public void destroyApp(boolean unconditional) {
		canvas.stop();
	}
}
