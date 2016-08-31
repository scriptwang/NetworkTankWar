import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class NetClient {
	
	private static int UDP_PORT_START = 2002;
	private int udpPort;
	private TankClient tClient;
	public DatagramSocket dSocket = null;
	
	public NetClient(TankClient tClient) {
		udpPort =  UDP_PORT_START;
		this.tClient = tClient;
		
		try {
			dSocket = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	
	public void connect(String ip , int port){
		Socket s = null;
		
		try {
			s = new Socket(ip, port);
System.out.println("client has connected !");
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			/**
			 * 将udpPort发给服务器
			 */
			dos.writeInt(udpPort);
System.out.println("udp port has sent !");
			/**
			 * 读取服务器发过来的ID号，并赋值给主战坦克
			 */
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			tClient.tank.id = id;
			
			if (id % 2 != 0) tClient.tank.good = false;
			else tClient.tank.good = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (s != null){
				try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		new Thread(new UDPDataReceiver()).start();
		System.out.println("Receive thread is start...");
		
		TankMsg tankMsg = new TankMsg(tClient.tank , tClient);
		send(tankMsg);
		
		
		
		
	}
	
	public void send(Message message){
		String IP = PropertyManager.getProperty("IP");
		message.send(dSocket, IP , TankServer.UDP_PORT);
		
	}
	
	/**
	 * A new Thread for receiving dPacket from server endless
	 */
	class UDPDataReceiver implements Runnable{
		byte[] buf = new byte[1024];
		
		@Override
		public void run(){
			
			while (dSocket != null){
				
				try {
					DatagramPacket dPacket = new DatagramPacket(buf, buf.length);
					dSocket.receive(dPacket);
System.out.println("Receiving a message!!!!!!!");
					parsePacket(dPacket);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
		public void parsePacket(DatagramPacket dPacket) throws IOException{
			
			ByteArrayInputStream bais = new ByteArrayInputStream(buf , 0 , dPacket.getLength());
			DataInputStream dis = new DataInputStream(bais);
			
			
			/**
			 * 根据messageType来处理不同的消息
			 */
			Message message = null;
			int messageType = dis.readInt();
			
			switch (messageType){
			case Message.TANK_NEW_MESSAGE:
				message = new TankMsg(tClient.tank,NetClient.this.tClient);
				message.parsePacket(dis);
				break;
			case Message.TANK_MOVE_MESSAGE:
				message = new TankMoveMessage(NetClient.this.tClient);
				message.parsePacket(dis);
				break;
			case Message.MISSILE_MESSAGE:
				message = new MissileMessage(NetClient.this.tClient);
				message.parsePacket(dis);
				break;
			case Message.TANK_DEAD_MESSAGE:
				message = new TankDeadMessage(NetClient.this.tClient);
				message.parsePacket(dis);
				break;
			case Message.KNOCK_TANK_MESSAGE:
				message = new KnockTankMessage(NetClient.this.tClient);
				message.parsePacket(dis);
				break;
			}
			
		}
	}
	
	
}
