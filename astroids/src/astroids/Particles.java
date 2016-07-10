// Astroids (J2ME) - (C) 2011 Ricardo Cruz <rpmcruz0@gmail.com>

package astroids;
import javax.microedition.lcdui.Graphics;
import libs.*;
import vecgraphics.*;

public class Particles
{
	private static final int LIFETIME = 20;
	private Vector cpos;
	private Particle particles[];
	private int cycles = 0;
	
	public Particles(int cx, int cy) {
		cpos = new Vector(cx, cy);
		particles = new Particle[20];
		for(int i = 0; i < particles.length; i++)
			particles[i] = new Particle(cx, cy);
	}

	public void paint(Graphics g) {
		for(int i = 0; i < particles.length; i++)
			particles[i].paint(g);
	}

	public void update() {
		for(int i = 0; i < particles.length; i++)
			particles[i].update();
		cycles++;
	}

	public boolean isAlive() {
		return cycles < LIFETIME;
	}
	
	public class Particle {
		private static final float VEL = 4.f;
		private Model model;
		private Vector pos;
		private float angle;
		
		public Particle(int x, int y) {
			model = new Model(2);
			model.setPoint(0, new Vector(-1, 0));
			model.setPoint(1, new Vector(+1, 0));
			model.setScale(4);
			pos = new Vector(x, y);
			angle = (float)(MyRandom.get().nextFloat() * Math.PI*2);
		}

		public void paint(Graphics g) {
//			g.setColor(0);
			model.paint(g, pos, angle);
		}
		
		public void update() {
			pos.x += VEL * Math.cos(angle);
			pos.y += VEL * Math.sin(angle);
		}
	}
}
