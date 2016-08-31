import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile {
	public static final int MISSILEWIDTH = Integer.parseInt(PropertyManager.getProperty("missileWidth"));
	public static final int MISSILEHEIGHT = Integer.parseInt(PropertyManager.getProperty("missileHeight"));
	public static final int MISSILESPEED = Integer.parseInt(PropertyManager.getProperty("missileSpeed"));
	
	private int posX , posY;
	private Direction direction;
	private TankClient tClient;
	private Tank tank; 
	public boolean good;
	
	public boolean live = true;
	
	
	public Missile(int posX , int posY , Direction direction , TankClient tClient) {
		this.posX = posX;
		this.posY = posY;
		this.direction = direction;
		this.tClient = tClient;
	} 
	
	public Missile(int posX , int posY , Direction direction , TankClient tClient ,boolean good){
		this(posX, posY, direction, tClient);
		this.good = good;
	}
	
	
	
	public void draw(Graphics g){
		if (!live) return;
		
		Color c = g.getColor();
		if(good) {
			g.setColor(Color.red);
		}else {
			g.setColor(Color.green);
		}
		g.fillRect(posX, posY, MISSILEWIDTH, MISSILEHEIGHT);
		g.setColor(c);
		move();
		knockBorder();
	}
	
	private void move(){
		switch(direction){
			case L:
				posX -= MISSILESPEED;
				break;
			case LU:
				posX -= MISSILESPEED;
				posY -= MISSILESPEED;
				break;
			case U:
				posY -= MISSILESPEED;
				break;
			case RU:
				posX += MISSILESPEED;
				posY -= MISSILESPEED;
				break;
			case R:
				posX += MISSILESPEED;
				break;
			case RD:
				posX += MISSILESPEED;
				posY += MISSILESPEED;
				break;
			case D:
				posY += MISSILESPEED;
				break;
			case LD:
				posX -= MISSILESPEED;
				posY += MISSILESPEED;
				break;
			
		}
		
	}
	
	private void knockBorder(){
		if (posX < 0 || posY < 0 || posX > tClient.WINDOWWIDTH || posY > tClient.WINDOWHEIGHT){
			tClient.missiles.remove(this);
			live = false;
			
		}
		
	}
	
	public void knockWalls(List<Wall> walls){
		for (int i=0;i<walls.size();i++){
			Wall wall = walls.get(i);
			if (this.getRect().intersects(wall.getRect())){
				tClient.missiles.remove(this);
				live = false;
			}
		}
		
	}
	
	public Rectangle getRect(){
		return new Rectangle(posX, posY, MISSILEWIDTH, MISSILEHEIGHT);
	}

}
