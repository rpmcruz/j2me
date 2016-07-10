// Astroids J2ME - (C) 2011-2012 Ricardo Cruz <ric8cruz@gmail.com>

package libs;
import java.util.Random;

public class MyRandom
{
	private static MyRandom singleton = null;
	private Random gen;
	
	private MyRandom() {
		gen = new Random();
		gen.setSeed(System.currentTimeMillis());
	}
	
	public static Random get() {
		if(singleton == null)
			singleton = new MyRandom();
		return singleton.gen;
	}
}
