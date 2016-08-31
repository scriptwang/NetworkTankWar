import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


public class TankDeadMessage implements Message{
	public int messageType = Message.TANK_DEAD_MESSAGE;
	private int id;
	private TankClient tClient;
	
	
	public TankDeadMessage(int id,TankClient tClient) {
		this.id = id;
		this.tClient = tClient;
	}
	
	public TankDeadMessage(TankClient tClient) {
		this.tClient = tClient;
	}
	

	@Override
	public void send(DatagramSocket dSocket, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(messageType);
			dos.writeInt(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] buf = baos.toByteArray();
		DatagramPacket dPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP,udpPort));
		try {
			dSocket.send(dPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void parsePacket(DataInputStream dis) throws IOException {
		int id = dis.readInt();
		
		if (tClient.tank.id == id){
			return;
		}
		for (int i=0;i<tClient.tanks.size();i++){
			Tank tank = tClient.tanks.get(i);
			if (id == tank.id){
				tank.live = false;
				tClient.tanks.remove(tank);
				break;
			}
		}
		
		
	}

}
