// Microracers (J2ME) - (C) 2011 Ricardo Cruz <rpmcruz0@gmail.com>

package game;
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import libs.MyMath;
import libs.PaintForm;

public class MyCanvas extends GameCanvas implements Runnable
{
    private GameDesign design;
    private TiledLayer tiles;
    private LayerManager lm;

	private boolean done;
	private Car cars[];

	private static final int CHECKPOINT_TILE = 17;
	private static final int CHECKPOINTS_TOTAL = 4;
	private static final int CARS_TOTAL = 3;

	private static final double AI_SPEED = .75;
	private static final double HUMAN_SPEED = 2.25, HUMAN_ROT_SPEED = .060;

	private Image redCarImg, blueCarImg;
	
	public MyCanvas() {
		super(true);
		try {
			setFullScreenMode(true);
			init();
		} catch (Exception ex) {}
	}

	private void init() throws IOException {
		design = new GameDesign();
        tiles = design.getRoad();
        lm = new LayerManager();
		
		redCarImg = Image.createImage("/redcar.png");
		blueCarImg = Image.createImage("/bluecar.png");

		cars = new Car[CARS_TOTAL];
		cars[0] = new Car(redCarImg);
		for(int i = 0; i < CARS_TOTAL; i++) {
			if(i > 0)
				cars[i] = new Car(blueCarImg);
			lm.append(cars[i]);
			findNextCheckpoint(cars[i]);
		}
		int x = 120, y = 60;
		for(int i = 0; i < cars.length; i += 2) {
			cars[i].setPosition(x, y);
			if(i+1 < cars.length)
				cars[i+1].setPosition(x, y+18);
			x -= 20;
		}

        design.updateLayerManagerForCircle(lm);
	}
	
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		done = true;
	}
	
    public void run() {
		done = false;
        Graphics g = getGraphics();
        while (!done) {
			long t = System.currentTimeMillis();
			
			movePlayer(cars[0], getKeyStates());
			for(int i = 1; i < cars.length; i++)
				moveComputer(cars[i]);

			for(int i = 0; i < cars.length; i++) {
				checkTiles(cars[i]);
				cars[i].updateFrame();
				cars[i].ranking = i;
			}
			calcRanking();
			centerCamera(cars[0]);

            lm.paint(g, 0, 0);
			drawRanking(g);
            flushGraphics(0, 0, getWidth(), getHeight());

			final int DELAY = 40;
			long dt = System.currentTimeMillis() - t;

			if(DELAY > dt)
				try { Thread.sleep(DELAY - dt); } catch (Exception ex) {}
        }
    }

	private void calcRanking() {
		for(int i = 0; i < cars.length; i++)
			for(int j = i+1; j < cars.length; j++) {
				int order = rankOrder(cars[i], cars[j]);
				if((order > 0 && cars[i].ranking > cars[j].ranking) ||
					(order < 0 && cars[i].ranking < cars[j].ranking)) {
					int t = cars[i].ranking;
					cars[i].ranking = cars[j].ranking;
					cars[j].ranking = t;
				}
			}
	}

	private void drawRanking(Graphics g) {
		final int ICON_WIDTH = 14, ICON_HEIGHT = 13, OUTER_BORDER = 3, INNER_BORDER = 2;
		int y = getHeight() - cars.length*ICON_HEIGHT - INNER_BORDER*2 - OUTER_BORDER;
		PaintForm.drawButton(g, OUTER_BORDER, y,
			ICON_WIDTH + INNER_BORDER*2, cars.length*ICON_HEIGHT + INNER_BORDER*2, false);
		for(int ranking = 0; ranking < cars.length; ranking++)
			for(int i = 0; i < cars.length; i++)
				if(cars[i].ranking == ranking) {
					cars[i].drawIcon(g, 4, y);
					y += ICON_HEIGHT;
					break;
				}
	}

	private int rankOrder(Car a, Car b) {
		if(a.checkpoints == b.checkpoints) {
			int cp_x = a.nextCheckpointX, cp_y = a.nextCheckpointY;
			int a_x = a.getRefPixelX(), a_y = a.getRefPixelY();
			int b_x = b.getRefPixelX(), b_y = b.getRefPixelY();
			int distA = MyMath.calcDistanceSq(a_x, a_y, cp_x, cp_y);
			int distB = MyMath.calcDistanceSq(b_x, b_y, cp_x, cp_y);
			if(distA == distB)
				return +1;  // never return 0
			return distB - distA;
		}
		return a.checkpoints - b.checkpoints;
	}
	
	private void checkTiles(Car car) {
		int xcell = car.getRefPixelX() / 16, ycell = car.getRefPixelY() / 16;
		//int tile = getTile(xcell, ycell);

		// check checkpoint
		float distx = (car.getX()-car.nextCheckpointX);
		float disty = (car.getY()-car.nextCheckpointY);
		float dist = distx*distx + disty*disty;

		if(dist < 30*30) {
System.out.println("reached check point: " + car.nextCheckpoint);
			car.nextCheckpoint = (car.nextCheckpoint+1) % CHECKPOINTS_TOTAL;
			car.checkpoints++;
			findNextCheckpoint(car);
		}
		
/*		for(int dx = -1; dx <= +1; dx++)
			for(int dy = -1; dy <= +1; dy++) {
				int cp = getTile(xcell+dx, ycell+dy) - CHECKPOINT_TILE;
				if(cp == car.nextCheckpoint) {
					car.nextCheckpoint = (car.nextCheckpoint+1) % CHECKPOINTS_TOTAL;
					car.checkpoints++;
					findNextCheckpoint(car);
				}
			}*/
	}

	private void findNextCheckpoint(Car car) {
		for(int x = 0; x < tiles.getColumns(); x++)
			for(int y = 0; y < tiles.getRows(); y++) {
				int cp = tiles.getCell(x, y) - CHECKPOINT_TILE;
				if(cp == car.nextCheckpoint) {
					car.nextCheckpointX = x * 16;
					car.nextCheckpointY = y * 16;
					return;
				}
			}
	}
/*	
	private int getTile(int xcell, int ycell) {
		if(xcell < 0 || xcell >= tiles.getColumns() ||
				ycell < 0 || ycell >= tiles.getRows())
			return 0;
		return tiles.getCell(xcell, ycell);
	}
*/
	private void movePlayer(Car car, int keys) {
		if((keys & LEFT_PRESSED) != 0)
			car.turn(+HUMAN_ROT_SPEED);
		if((keys & RIGHT_PRESSED) != 0)
			car.turn(-HUMAN_ROT_SPEED);
		car.move(+HUMAN_SPEED);
/*		if((keys & UP_PRESSED) != 0)
			car.move(+HUMAN_SPEED);
		if((keys & DOWN_PRESSED) != 0)
			car.move(-HUMAN_SPEED/2);*/
	}

	private void moveComputer(Car car) {
		double cp_angle = MyMath.calcAngleBetween(
			car.getX(), car.getY(), car.nextCheckpointX, car.nextCheckpointY);
		double dangle = MyMath.normalizeAngle(car.getAngle() - cp_angle);
		final double IGNORE_ANGLE = 0.05;
		if(dangle > IGNORE_ANGLE && dangle < 2*Math.PI - IGNORE_ANGLE)
			car.turn(dangle > Math.PI ? +0.1 : -0.1);
		car.move(+AI_SPEED);
	}

	private void centerCamera(Car car) {
		int x = car.getRefPixelX() - getWidth()/2;
		int y = car.getRefPixelY() - getHeight()/2;

		int xmin = tiles.getX();
		int ymin = tiles.getY();
		int xmax = tiles.getX() + tiles.getWidth() - getWidth();
		int ymax = tiles.getY() + tiles.getHeight() - getHeight();
		x = (x < xmin) ? xmin : x;
		x = (x > xmax) ? xmax : x;
		y = (y < ymin) ? ymin : y;
		y = (y > ymax) ? ymax : y;
		
        lm.setViewWindow(x, y, getWidth(), getHeight());
	}
}
