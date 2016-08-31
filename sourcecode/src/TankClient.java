import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TankClient extends Frame {

	public static final int WINDOWWIDTH = Integer.parseInt(PropertyManager.getProperty("windowWidth")); 
	public static final int WINDOWHEIGHT = Integer.parseInt(PropertyManager.getProperty("windowHeight"));
	public static final int REPAINTSPEED = Integer.parseInt(PropertyManager.getProperty("repaintSpeed"));
	
	private Image offScreen = null;
	private Color backColor = Color.black;
	
	//public Tank tank = new Tank(200, 250, Direction.STOP, this , true);
	public Tank tank = new Tank(new Random().nextInt(WINDOWWIDTH) - 100 , new Random().nextInt(WINDOWHEIGHT) - 100, Direction.STOP, this , true);
	public NetClient nClient = new NetClient(this);
	
	
	public List<Missile> missiles = new ArrayList<Missile>();
	public List<Tank> tanks = new ArrayList<Tank>();
	public List<Explode> explodes = new ArrayList<Explode>();
	public List<Wall> walls = new ArrayList<Wall>();
	public List<Blood> bloods = new ArrayList<Blood>();
	
	
	
	
	
	public static void main(String[] args) {

		new TankClient().launchFrame();
		//new TankClient().launchFrame();
	}
	
	private void addBloods(){
		for (int i=0;i<10;i++){
			bloods.add(new Blood(this));
		}
	}
	
	private void addTanks(){
		for (int i=0;i<Integer.parseInt(PropertyManager.getProperty("produceTankNum"));i++){
			tanks.add(new Tank(50, 50+50*i, Direction.randonDirection(), this, false));
		}
	}
	
	private void launchFrame(){
		//addTanks();
		//addBloods();
		
		walls.add(new Wall(150, 200,10,100 , this));
		walls.add(new Wall(300, 200,10,100 , this));
		walls.add(new Wall(150, 200,150,10 , this));
		walls.add(new Wall(150, 300,50,10 , this)); 
		walls.add(new Wall(260, 300,50,10 , this));
		walls.add(new Wall(200, 300,10,50 , this));
		walls.add(new Wall(260, 300,10,50 , this));
		
		
		//setTitle("TANK WAR ID : " + tank.id);
		setBounds(500, 180, WINDOWWIDTH, WINDOWHEIGHT);
		setVisible(true);
		setBackground(backColor);
		setResizable(false);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}	
		});
		addKeyListener(new KeyMonitor());
		
		new Thread(new RePaint()).start();
		String IP = PropertyManager.getProperty("IP");
		nClient.connect(IP,TankServer.TCP_PORT);
		
		setTitle("TANK WAR ID : " + tank.id);
	}
	
	@Override
	public void paint(Graphics g){
		/*if (bloods.size() <= 0){
			addBloods();
		}*/
		
		/*if (tanks.size() <= 0){
			addTanks();
		}*/
		
		
		tank.draw(g);
		tank.hitTanks(missiles);
		tank.knockWalls(walls);
		tank.knockTanks(tanks);
		tank.eatBloods(bloods);
		
		/**
		 * 画出每个List
		 */
		for (int i=0;i<missiles.size();i++){
			Missile missile = missiles.get(i);
			missile.knockWalls(walls);
			missile.draw(g);
		}
		
		for (int i=0;i<tanks.size();i++){
			Tank tank = tanks.get(i);
			tank.draw(g);
			tank.hitTanks(missiles);
			tank.knockWalls(walls);
			tank.knockTanks(tanks);
			tank.eatBloods(bloods);
		}
		
		for (int i=0;i<explodes.size();i++){
			Explode explode = explodes.get(i);
			explode.draw(g);
		}
		
		for (int i=0;i<walls.size();i++){
			Wall wall = walls.get(i);
			wall.draw(g);
		}
		
		for (int i=0;i<bloods.size();i++){
			Blood blood = bloods.get(i);
			blood.draw(g);
		}
		
		g.setColor(Color.red);
		g.drawString("Missile count : " + missiles.size(), 10, 50);
		g.drawString("Tank count : " + tanks.size(), 10, 80);
		g.drawString("Explode count :" + explodes.size(), 10, 100);
		g.drawString("Blood count :" + bloods.size(), 10, 120);
		
	}
	
	
	/**
	 * 双缓冲除闪烁：先将屏幕画在一张图片上，再将图片画在屏幕上，涉及到两支画笔（图片画笔和当前屏幕画笔）
	 */
	@Override
	public void update(Graphics g){
		if ( offScreen == null){
			offScreen = createImage(WINDOWWIDTH, WINDOWHEIGHT);
		}
		Graphics sg = offScreen.getGraphics();
		sg.setColor(backColor);
		sg.fillRect(0, 0, WINDOWWIDTH, WINDOWHEIGHT);
		paint(sg);
		g.drawImage(offScreen, 0, 0, null);
		
	}
	
	
	class RePaint extends Thread{
		
		public void run(){
			while(true){
				try {
					Thread.sleep(REPAINTSPEED);
					repaint();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class KeyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			tank.KeyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			tank.keyReleased(e);
		}
		
	}
	
	
}



