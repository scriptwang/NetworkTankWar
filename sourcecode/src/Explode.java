import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Explode {

	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	private static Image[] explodeImages = null;
	private static int index = 0;
	
	private int posX,posY; 
	private TankClient tClient;
	private boolean live = true;
	
	static {
		explodeImages = new Image[] {
				toolkit.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
				toolkit.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
				toolkit.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
				toolkit.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
				toolkit.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
				toolkit.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
				toolkit.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
				toolkit.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
				toolkit.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
				toolkit.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
				toolkit.getImage(Explode.class.getClassLoader().getResource("images/10.gif"))
		};
	}
	
	public Explode(int posX,int posY,TankClient tClient) {
		this.posX = posX;
		this.posY = posY;
		this.tClient = tClient;
	}
	
	public void draw(Graphics g){
		if (!live) return;
		
		if (index == explodeImages.length){
			live = false;
			index = 0;
			tClient.explodes.remove(this);
		}
		
		g.drawImage(explodeImages[index], posX, posY, null);
		index ++;
	}
	
	
	
}
