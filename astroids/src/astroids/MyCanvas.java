// Astroids J2ME - (C) 2011-2012 Ricardo Cruz <ric8cruz@gmail.com> //

package astroids;
import libs.MyRandom;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public class MyCanvas extends GameCanvas implements Runnable
{
	private boolean gameover;
	private boolean done = false;
	
	private Ship ship;
	private Astroid astroids[];
	private boolean shooting = false;
	private boolean inverseColors = false;
	private int level = 0;

	public static float scale = 1;
	
	public MyCanvas() {
		super(true);
		setFullScreenMode(true);
		scale = getWidth() / 240.f;
	}

	private void restart() {
		ship = new Ship(getWidth()/2, getHeight()/2);
		astroids = new Astroid[4 + level*2];
		final int ASTROID_PADDING = 75;
		for(int i = 0; i < astroids.length; i++) {
			double angle = MyRandom.get().nextFloat() * Math.PI*2;
			astroids[i] = new Astroid(
				(int)(getWidth()/2 + ASTROID_PADDING*Math.cos(angle)),
				(int)(getHeight()/2 + ASTROID_PADDING*Math.sin(angle)));
		}
		gameover = false;
		inverseColors = !inverseColors;
	}
	
    // main loop
	public void run() {
		ParticleSystem particles = new ParticleSystem();
		Bullet bullet = null;

		Graphics g = getGraphics();
		int dispWidth = getWidth();
		int dispHeight = getHeight();
		restart();

        while (!done) {
			long t = System.currentTimeMillis();

            int keys = getKeyStates();
			if(gameover) {
				if((keys & GameCanvas.FIRE_PRESSED) != 0 && !shooting)
					restart();
			}
			else {
				// keys
				ship.processKeys(keys);
				ship.insideScreen(dispWidth, dispHeight);
				if((keys & GameCanvas.FIRE_PRESSED) != 0 && !shooting)
					if(bullet == null)
						bullet = new Bullet(ship.getPos(), ship.getAngle());
			}

			shooting = (keys & GameCanvas.FIRE_PRESSED) != 0;
			
			// update
			for(int i = 0; i < astroids.length; i++)
				if(astroids[i] != null) {
					astroids[i].update();
					astroids[i].insideScreen(dispWidth, dispHeight);
				}
			particles.update();
			if(bullet != null) {
				bullet.update();
				bullet.insideScreen(dispWidth, dispHeight);
				if(!bullet.isAlive())
					bullet = null;
			}

			// collisions
			for(int i = 0; i < astroids.length; i++)
				if(astroids[i] != null) {
					if(bullet != null && astroids[i].collides(bullet)) {
						particles.create((int)astroids[i].getPos().x, (int)astroids[i].getPos().y);
						astroids[i] = null;
						
						int astroids_nb = 0;
						for(int j = 0; j < astroids.length; j++)
							if(astroids[j] != null)
								astroids_nb++;
						if(astroids_nb == 0) {
							gameover = true;
							level++;
						}
					}
					else if(!gameover && astroids[i].collides(ship)) {
						particles.create((int)ship.getPos().x, (int)ship.getPos().y);
						gameover = true;
						level = 0;
						ship = null;
					}
				}

			// paint
			if(inverseColors)
				g.setColor(0xFFFFFF);
			else
				g.setColor(0);
			g.fillRect(0, 0, dispWidth, dispHeight);

			if(inverseColors)
				g.setColor(0);
			else
				g.setColor(0xFFFFFF);
			if(ship != null)
				ship.paintAllSides(g, dispWidth, dispHeight);
			for(int i = 0; i < astroids.length; i++)
				if(astroids[i] != null)
					astroids[i].paint(g, dispWidth, dispHeight);
			particles.paint(g);
			if(bullet != null)
				bullet.paint(g);
            flushGraphics(0, 0, dispWidth, dispHeight);

			final int DELAY = 40;
			long dt = System.currentTimeMillis() - t;

			if(DELAY > dt)
				try { Thread.sleep(DELAY - dt); } catch (Exception ex) {}
        }
    }

    public void stop() {
		done = true;
    }
}
