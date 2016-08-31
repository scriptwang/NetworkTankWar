import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Blood {
	public final int WIDTH = 10;
	public final int HEIGHT = 10;
	private int posX = new Random().nextInt(Integer.parseInt(PropertyManager.getProperty("windowWidth")));
	private  int posY = new Random().nextInt(Integer.parseInt(PropertyManager.getProperty("windowHeight")));
	public boolean live = true;
	
	private TankClient tClient;
	
	
	public Blood(TankClient tClient) {
		this.tClient = tClient;
	}
	
	public void draw(Graphics g){
		if (!live) {
			tClient.bloods.remove(this);
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.orange);
		g.fillRect(posX, posY, WIDTH, HEIGHT);
		g.setColor(c);
	}
	
	public Rectangle getRect(){
		return new Rectangle(posX, posY, WIDTH, HEIGHT);
	}
}
