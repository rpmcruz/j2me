// Astroids J2ME - (C) 2011-2012 Ricardo Cruz <ric8cruz@gmail.com> //

package astroids;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class Astroids extends MIDlet
{
    private MyCanvas canvas;

    public void startApp() {
        canvas = new MyCanvas();
		Thread thread = new Thread(canvas);
        thread.start();
        Display display = Display.getDisplay(this);
        display.setCurrent(canvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        canvas.stop();
    }
}
