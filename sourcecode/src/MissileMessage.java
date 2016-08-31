import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class MissileMessage implements Message{	
	
	public int messageType = Message.MISSILE_MESSAGE;
	private int posX , posY;
	private Direction direction;
	private TankClient tClient;
	private boolean good;
	private int id;
	
	public MissileMessage(int posX, int posY, Direction direction, TankClient tClient,boolean good, int id ) {
		this.posX = posX;
		this.posY = posY;
		this.direction = direction;
		this.tClient = tClient;
		this.good = good;
		this.id = id;
	}
	
	public MissileMessage(TankClient tClient) {
		this.tClient = tClient;
	}
	
	@Override
	public void send(DatagramSocket dSocket, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(messageType);
			dos.writeInt(id);
			dos.writeInt(posX);
			dos.writeInt(posY);
			dos.writeInt(direction.ordinal());
			dos.writeBoolean(good);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		byte[] buf = baos.toByteArray();
		DatagramPacket dPacket = new DatagramPacket(buf, buf.length ,new InetSocketAddress(IP, udpPort));

		try {
			dSocket.send(dPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	@Override
	public void parsePacket(DataInputStream dis) throws IOException {
		int id = dis.readInt();
		int posX = dis.readInt();
		int posY = dis.readInt();
		Direction direction = Direction.values()[dis.readInt()];
		boolean good = dis.readBoolean();
		
		if (tClient.tank.id == id){
			return;
		}
		tClient.missiles.add(new Missile(posX, posY, direction, tClient, good));
	}
	
	
	

}
