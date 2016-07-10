// Astroids (J2ME) - (C) 2011 Ricardo Cruz <rpmcruz0@gmail.com>

package astroids;
import javax.microedition.lcdui.Graphics;

public class ParticleSystem
{
	Node node = null;
	
	public ParticleSystem() {
	}
	
	public void create(int cx, int cy) {
		Node n = node;
		node = new Node();
		node.p = new Particles(cx, cy);
		node.next = n;
	}
	
	public void paint(Graphics g) {
		for(Node n = node; n != null; n = n.next)
			n.p.paint(g);
	}
	
	public void update() {
		Node prev = null;
		for(Node n = node; n != null; prev = null, n = n.next) {
			n.p.update();
			if(!n.p.isAlive()) {
				if(prev == null)
					node = null;
				else
					prev.next = n.next;
			}
		}
	}
	
	private static class Node {
		public Node next;
		public Particles p;
	}
}
