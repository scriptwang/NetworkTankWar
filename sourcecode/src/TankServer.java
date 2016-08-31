import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class TankServer {
	
	
	private static int ID = 100;
	public static final int TCP_PORT = 8888;
	public static final int UDP_PORT = 6666;
	
	List<Client> clients = new ArrayList<Client>();
	
	public static void main(String[] args){
		new TankServer().start();
		
	}
	
	public void start(){
		new Thread(new UDPThread()).start();
		
		
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(TCP_PORT);
			System.out.println("Server started ... ");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		while(true){
			Socket s = null;
			try{
				s = ss.accept();
				System.out.println("a client has connected " + s.getInetAddress() + ":" + s.getPort());
				/**
				 * 读取客户端发来的udp连接信息（ip 和 udp port），读完后new一个Client对象并装进List中 
				 * 最后关闭Socket s
				 */
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int udpPort = dis.readInt();
				String IP = s.getInetAddress().getHostAddress();
				clients.add(new Client(IP, udpPort));
				
				/**
				 * 将独一无二的id号发回客户端
				 * 这个accept()处理完了才有可能处理下一个客户端，因此没有两个客户端抢着要ID的情况
				 */
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(ID ++);
				
				printClients();
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				if (s != null){
					try {
						s.close();
						s = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
			 	}
				
			}
		}
		
		
	}
	
	private void printClients(){
		for (int i=0;i<clients.size();i++){
			System.out.println(clients.get(i).IP + ":" + clients.get(i).udpPort );
		}
	}
	
	
	class Client{
		
		String IP;
		int udpPort;
		
		public Client(String IP , int udpPort){
			this.IP = IP;
			this.udpPort = udpPort;
		}
		
	}
	
	public class UDPThread implements Runnable{
		
		
		@Override
		public void run(){
			int count = 0;
			
			byte[] buf = new byte[1024];
			
			/**
			 * Give dSocket a initialization
			 */
			DatagramSocket dSocket = null;
			try {
				dSocket = new DatagramSocket(UDP_PORT);
			} catch (SocketException e) {
				e.printStackTrace();
			} 
			
			/**
			 * Receive dPacket from Client endless
			 */
			while (dSocket != null){
				try {
					DatagramPacket dPacket = new DatagramPacket(buf, buf.length);
					dSocket.receive(dPacket);
					/**
					 * Transpond every dPacket received 
					 */
					transpond(dPacket, dSocket);
					
				} catch (IOException e) {
					e.printStackTrace();
				}	
System.out.println("A packet has received :" + (count++));
			}
		}
		
		private void transpond(DatagramPacket dPacket , DatagramSocket dSocket) throws IOException{
			for (int i=0;i<clients.size();i++){
				Client client = clients.get(i);
				
				/**
				 * give dPacket received a new address and send to every Client
				 */
				dPacket.setSocketAddress(new InetSocketAddress(client.IP, client.udpPort));
				dSocket.send(dPacket);
			}
		}
		
		
		
	}
}
 