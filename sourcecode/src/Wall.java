import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {

	private int posX , posY,width,height;
	private TankClient tClient;

	public Wall(int posX, int posY, int width, int height, TankClient tClient) {
		super();
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.tClient = tClient;
	}
	
	
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.BLUE);
		g.fillRect(posX, posY, width, height);
		g.setColor(c);
	}
	
	public Rectangle getRect(){
		return new Rectangle(posX, posY, width, height);
	}
}
