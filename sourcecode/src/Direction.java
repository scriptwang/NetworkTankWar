import java.util.Random;

public enum Direction {
	L,LU,U,RU,R,RD,D,LD,STOP;
	
	public static Direction randonDirection(){
		/**
		 * Direction.values()���ص���Directin������
		 */
		Direction[]  directions = Direction.values();
		
		int randonNum = new Random().nextInt(directions.length);
		return directions[randonNum];
		
	}
	
	public static Direction randonDirectionNoStop(){
		/**
		 * Direction.values()���ص���Directin������
		 */
		Direction[]  directions = Direction.values();
		
		int randonNum = new Random().nextInt(directions.length - 1);
		return directions[randonNum];
		
	}

}
