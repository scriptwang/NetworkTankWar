import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class KnockTankMessage implements Message{
	public int messageType = Message.KNOCK_TANK_MESSAGE;
	private int id;
	private int oldX , oldY;
	private TankClient tClient;
	
	public KnockTankMessage(int oldX , int oldY , int id , TankClient tClient) {
		this.oldX = oldX;
		this.oldY = oldY;
		this.id = id;
		this.tClient = tClient;
	}
	
	public KnockTankMessage(TankClient tClient) {
		this.tClient = tClient;
	}
	

	@Override
	public void send(DatagramSocket dSocket, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(messageType);
			dos.writeInt(id);
			dos.writeInt(oldX);
			dos.writeInt(oldY);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		byte[] buf = baos.toByteArray();
		DatagramPacket dPacket = new DatagramPacket(buf, buf.length,new InetSocketAddress(IP, udpPort));
		try {
			dSocket.send(dPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void parsePacket(DataInputStream dis) throws IOException {
		int id = dis.readInt();
		int oldX = dis.readInt();
		int oldY = dis.readInt();
		
		if(tClient.tank.id == id){
			return;
		}
		
		
		for(int i=0;i<tClient.tanks.size();i++){
			Tank tank = tClient.tanks.get(i);
			if (tank.id == id){
				tank.posX = oldX;
				tank.posY = oldY;
			}
		}
		
	}

}
