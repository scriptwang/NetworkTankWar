import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramSocket;

public interface Message {
	public static final int TANK_NEW_MESSAGE = 1;
	public static final int TANK_MOVE_MESSAGE = 2;
	public static final int MISSILE_MESSAGE = 3;
	public static final int TANK_DEAD_MESSAGE = 4;
	public static final int KNOCK_TANK_MESSAGE = 5;
	
	public void send(DatagramSocket dSocket , String IP , int udpPort);
	public void parsePacket(DataInputStream dis) throws IOException;

}
