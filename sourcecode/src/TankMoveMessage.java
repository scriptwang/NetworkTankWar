import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


public class TankMoveMessage implements Message{
	
	public int messageType = Message.TANK_MOVE_MESSAGE;
	private TankClient tClient;
	private int id;
	private int life;
	private int posX , posY;
	private Direction gunDirection;
	private Direction Direction;
	
	public TankMoveMessage(TankClient tClient,int life,int posX , int posY,int id,Direction gunDirection,Direction direction) {
		this.tClient = tClient;
		this.life = life;
		this.posX = posX;
		this.posY = posY;
		this.id = id;
		this.gunDirection = gunDirection;
		this.Direction = direction;
		
	}
	
	public TankMoveMessage(TankClient tClient) {
		this.tClient = tClient;
	}

	@Override
	public void send(DatagramSocket dSocket, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(messageType);
			dos.writeInt(id);
			dos.writeInt(life);
			dos.writeInt(posX);
			dos.writeInt(posY);
			dos.writeInt(gunDirection.ordinal());
			dos.writeInt(Direction.ordinal());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		byte[] buf = baos.toByteArray();
		DatagramPacket dPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, udpPort));
		try {
			dSocket.send(dPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void parsePacket(DataInputStream dis) throws IOException {
		//int messageType = dis.readInt();
		int id = dis.readInt();
		int life = dis.readInt();
		int posX = dis.readInt();
		int posY = dis.readInt();
		Direction gunDirection = Direction.values()[dis.readInt()];
		Direction direction = Direction.values()[dis.readInt()];
		
		//tClient.tanks.add(new Tank(posX, posY, direction, tClient , true));
		
		System.out.println("id:"+id+",posX:" + posX+",posY:"+posY+",Dir:"+direction.toString());
		
		/**
		 * 遍历找到发Moving message的这个坦克，然后将新的位置信息赋给它
		 */
		boolean exists = false;
		
		for (int i=0; i<tClient.tanks.size(); i++){
			Tank tank = tClient.tanks.get(i);
			if (tank.id == id){
				tank.life = life;
				tank.posX = posX;
				tank.posY = posY;
				tank.gunDirection = gunDirection;
				tank.direction = direction;
				exists = true;
				break;//找到了就berak不再做循环了
			}
		}
		
		
	}
	

}
