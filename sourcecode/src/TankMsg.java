import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankMsg  implements Message{
	
	public int messageType = Message.TANK_NEW_MESSAGE;
	
	private Tank tank;
	private TankClient tClient;
	
	public TankMsg(Tank tank , TankClient tClient) {
		this.tank = tank;
		this.tClient = tClient;
	}
	
	public TankMsg(){};

	public void send(DatagramSocket dSocket , String IP , int port) {
		ByteArrayOutputStream baos =  new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(messageType);
			dos.writeInt(tank.id);
			dos.writeInt(tank.posX);
			dos.writeInt(tank.posY);
			dos.writeInt(tank.direction.ordinal());
			dos.writeBoolean(tank.good);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] buf = baos.toByteArray();
		DatagramPacket dPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP,port));
		try {
			dSocket.send(dPacket);
System.out.println("packet has sended =========");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void parsePacket(DataInputStream dis) throws IOException {
		//int messageType = dis.readInt();
		int id = dis.readInt();
		
		if (id == tank.id){
			return;//如果是自己，那么久return
		}
		
		
		int posX = dis.readInt();
		int posY = dis.readInt();
		Direction direction = Direction.values()[dis.readInt()];
		boolean good = dis.readBoolean();
System.out.println("id:" + id+" posX:" + posX+" posY:" + posY + " direction:" + direction  + " good:" + good);


		/**
		 * 遍历判断List tanks里面有没有这辆坦克，如果存在则break
		 * 不存在，再new一辆坦克加入List tanks
		 */
		boolean exists = false;
		for (int i=0;i<tClient.tanks.size();i++){
			Tank tank = tClient.tanks.get(i);
			if (tank.id == id){
				exists = true;
				break;
			}
		}
		
		if (!exists){
			TankMsg tankMsg = new TankMsg(tClient.tank,tClient);
			tClient.nClient.send(tankMsg);
			
			tClient.tanks.add(new Tank(posX, posY, direction, tClient , good , id));
		}
		
		
		
	}

}
