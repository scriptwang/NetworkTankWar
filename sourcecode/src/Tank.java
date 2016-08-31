import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;


public class Tank {

	public static final int TANKWIDTH = Integer.parseInt(PropertyManager.getProperty("tankWidth"));
	public static final int TANKHEIGHT = Integer.parseInt(PropertyManager.getProperty("tankHeight"));
	
	
	private boolean L = false,U = false,R = false,D = false;
	public boolean good;
	public int posX,posY;
	private int oldX,oldY;
	private int tankSpeed = Integer.parseInt(PropertyManager.getProperty("tankSpeed"));
	public Direction direction ;
	public Direction gunDirection = Direction.randonDirection(); 
	private TankClient tClient;
	public boolean live = true;
	public int life = 100;
	private static Random r = new Random();
	
	public int id;
	
	public Tank(int posX,int posY,Direction direction,TankClient tClient) {
		this.posX = posX;
		this.posY = posY;
		this.direction = direction;
		this.tClient = tClient;
	}
	
	public Tank(int posX,int posY,Direction direction,TankClient tClient,boolean good){
		this(posX,posY,direction,tClient);
		this.good = good;
	}
	
	public Tank(int posX,int posY,Direction direction,TankClient tClient,boolean good,int id){
		this(posX, posY, direction, tClient , good);
		this.id = id;
	}
	
	public void draw(Graphics g){
		if (!live) return;
		Color c = g.getColor();
	
		if (good) g.setColor(Color.white);
		else g.setColor(Color.yellow);
		g.fillOval(posX, posY, TANKWIDTH, TANKHEIGHT);
		g.drawString(""+life, posX + 4, posY - 2);
		g.drawString(""+id, posX + 4, posY - 12);
		g.setColor(c);
		gun(g);
		move();
		//enemyTank(); //不调用坏坦克的随即移动 发子弹的方法
		knockBorder();
	}

	public void KeyPressed(KeyEvent e) {
		if (!live) return;
		
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:
			L = true;
			break;
		case KeyEvent.VK_UP:
			U = true;
			break;
		case KeyEvent.VK_RIGHT:
			R = true;
			break;
		case KeyEvent.VK_DOWN:
			D = true;
			break;
		case KeyEvent.VK_F:
			fire();
			break;
		case KeyEvent.VK_S:
			superFire();
			break;
		case KeyEvent.VK_F2:
			live = true;
			life = 100;
			break;
		}

		loadDirection();
		
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		switch (key) {
		case KeyEvent.VK_LEFT:
			L = false;
			break;
		case KeyEvent.VK_UP:
			U = false;
			break;
		case KeyEvent.VK_RIGHT:
			R = false;
			break;
		case KeyEvent.VK_DOWN:
			D = false;
			break;
		}
	}
	
	private void loadDirection(){
		Direction oldDirection = direction;
		
		if(L == true && U == false && R == false && D == false) direction = Direction.L;
		else if(L == false && U == true && R == false && D == false) direction = Direction.U;
		else if(L == false && U == false && R == true && D == false) direction = Direction.R;
		else if(L == false && U == false && R == false && D == true) direction = Direction.D;
		else if(L == true && U == true && R == false && D == false) direction = Direction.LU;
		else if(L == false && U == true && R == true && D == false) direction = Direction.RU;
		else if(L == true && U == false && R == false && D == true) direction = Direction.LD;
		else if(L == false && U == false && R == true && D == true) direction = Direction.RD;
		
		if (direction != Direction.STOP){
			gunDirection = direction;
		}
		
		
		if (direction != oldDirection){
			TankMoveMessage moveMsg = new TankMoveMessage(tClient, life,posX, posY,id,gunDirection,direction);
			tClient.nClient.send(moveMsg);
		}
		
	}
	
	private void move(){
		oldX = posX;
		oldY = posY;
		switch(direction){
			case L:
				posX -= tankSpeed;
				break;
			case LU:
				posX -= tankSpeed;
				posY -= tankSpeed;
				break;
			case U:
				posY -= tankSpeed;
				break;
			case RU:
				posX += tankSpeed;
				posY -= tankSpeed;
				break;
			case R:
				posX += tankSpeed;
				break;
			case RD:
				posX += tankSpeed;
				posY += tankSpeed;
				break;
			case D:
				posY += tankSpeed;
				break;
			case LD:
				posX -= tankSpeed;
				posY += tankSpeed;
				break;
			case STOP:
				break;
		}
		
	}
	
	private void enemyTank(){
		if (!good){
			
			/**
			 * 让敌军坦克不停止
			 */
			if (direction == Direction.STOP){
				direction = Direction.randonDirection();
			}
			
			/**
			 * 让敌军坦克子弹不停止
			 */
			if (new Random().nextInt(100) > 95){
				Direction dir = Direction.randonDirectionNoStop();
				fire(dir);
			}
			
			/**
			 * 让敌军坦克随机运动
			 */
			if (new Random().nextInt(100) > 95){
				direction = Direction.randonDirection();
			}
			
		}
	}
	
	private void gun(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.black);
		switch (gunDirection) {
		case U:
			g.drawLine( posX + TANKWIDTH/2,posY + TANKHEIGHT/2 , posX + TANKWIDTH/2, posY);
			break;
		case RU:
			g.drawLine( posX + TANKWIDTH/2,posY + TANKHEIGHT/2 , posX + TANKWIDTH, posY);
			break;
		case R:
			g.drawLine( posX + TANKWIDTH/2,posY + TANKHEIGHT/2 , posX + TANKWIDTH, posY + TANKHEIGHT/2);
			break;
		case RD:
			g.drawLine( posX + TANKWIDTH/2,posY + TANKHEIGHT/2 , posX + TANKWIDTH, posY + TANKHEIGHT);
			break;
		case D:
			g.drawLine( posX + TANKWIDTH/2,posY + TANKHEIGHT/2 , posX + TANKWIDTH/2, posY + TANKHEIGHT);
			break;
		case LD:
			g.drawLine( posX + TANKWIDTH/2,posY + TANKHEIGHT/2 , posX , posY + TANKHEIGHT);
			break;
		case L:
			g.drawLine( posX + TANKWIDTH/2,posY + TANKHEIGHT/2 , posX, posY + TANKHEIGHT/2);
			break;
		case LU:
			g.drawLine( posX + TANKWIDTH/2,posY + TANKHEIGHT/2 , posX, posY);
			break;
		}
		g.setColor(c);
		
	}
	
	private void fire(){
		if (!live) return;
		int posX = this.posX + TANKWIDTH/2 - Integer.parseInt(PropertyManager.getProperty("missileWidth"))/2;
		int posY = this.posY + TANKHEIGHT/2 - Integer.parseInt(PropertyManager.getProperty("missileHeight"))/2;
		tClient.missiles.add(new Missile(posX, posY, gunDirection, tClient , good ));
		
		/**
		 * 给服务器发一个消息
		 */
		Message message = new MissileMessage(posX,posY,gunDirection,tClient,good,id);
		tClient.nClient.send(message);
	}
	
	private void fire(Direction direction){
		if (!live) return;
		int posX = this.posX + TANKWIDTH/2 - Integer.parseInt(PropertyManager.getProperty("missileWidth"))/2;
		int posY = this.posY + TANKHEIGHT/2 - Integer.parseInt(PropertyManager.getProperty("missileHeight"))/2;
		tClient.missiles.add(new Missile(posX, posY, direction, tClient , good ));
		
		Message message = new MissileMessage(posX,posY,direction,tClient,good,id);
		tClient.nClient.send(message);
	}
	
	private void superFire(){
		if (!live) return;
		Direction[] directions = Direction.values();
		for (int i=0;i<directions.length-1;i++){
			fire(directions[i]);
			
		}
	}
	
	
	private void knockBorder(){
		if (posX < 0){
			posX = tClient.WINDOWWIDTH;
		}else if (posY < 0){
			posY = tClient.WINDOWHEIGHT;
		}else if (posX > tClient.WINDOWWIDTH){
			posX = 0;
		}else if (posY > tClient.WINDOWHEIGHT){
			posY = 0;
		}
	}
	
	private void stay(){
		posX = oldX;
		posY = oldY;
	}
	
	public Rectangle getRect(){
		return new Rectangle(posX, posY, TANKWIDTH, TANKHEIGHT);
	}
	
	public boolean hitTanks(List<Missile> missiles){
		
		for (int i=0;i<missiles.size();i++){
			Missile missile = missiles.get(i);
			
			if (missile.getRect().intersects(getRect()) && this.good != missile.good && live == true && missile.live == true){
				
				if (life <= 1){
					
					this.live = false;
					tClient.tanks.remove(this);		
					
					Message message = new TankDeadMessage(id, tClient);
					tClient.nClient.send(message);
				}
				
				
				this.life -= 20;
				tClient.missiles.remove(missile);
				missile.live = false;
				tClient.explodes.add(new Explode(posX,posY,tClient));
				return true;
			}
		}
		
		return false;
	}
	
	
	
	public void knockWalls(List<Wall> walls){
		for (int i=0;i<walls.size();i++){
			Wall wall = walls.get(i);
			if (this.getRect().intersects(wall.getRect())){
				stay();
			}
		}
	}
	
	public void knockTanks(List<Tank> tanks ){
		if( !live ) return; //如果坦克死掉，就不用在不停的检测
		
		for (int i=0;i<tanks.size();i++){
			Tank tank = tanks.get(i);
			/**
			 * 语法糖语法糖语法糖语法糖语法糖语法糖语法糖语法糖语法糖语法糖语法糖语法糖语法糖语法糖语法糖语法糖
			 * (tank != this) 漂亮地把自身从tanks中去除！
			 */
			if (tank != this ){
				if (this.getRect().intersects(tank.getRect())){
					
					/**
					 * 撞到其他坦克的时候发一个消息给服务器
					 */
					Message kMessage = new KnockTankMessage(oldX, oldY, id, tClient);
					tClient.nClient.send(kMessage);
					
					this.life -= 1;
					if (life <= 1){
						
						this.live = false;
						tClient.tanks.remove(this);		
						tClient.explodes.add(new Explode(posX,posY,tClient));
						
						Message tMessage = new TankDeadMessage(id, tClient);
						tClient.nClient.send(tMessage);
					}
					
					stay();
				}
			}
		}
	}
	
	public boolean eatBloods(List<Blood> bloods){
		for (int i=0;i<bloods.size();i++){
			Blood blood = bloods.get(i);
			if (this.getRect().intersects(blood.getRect())){
				if (life < 100){
					this.life += new Random().nextInt(20);
					if (life > 100){
						life = 100;
					}
				}
				tClient.bloods.remove(blood);
				blood.live = false;
				return true;
			}
		}
		return false;
	}
	
	
	
}
