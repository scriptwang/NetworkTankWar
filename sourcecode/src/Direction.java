import java.util.Random;

public enum Direction {
	L,LU,U,RU,R,RD,D,LD,STOP;
	
	public static Direction randonDirection(){
		/**
		 * Direction.values()返回的是Directin的数组
		 */
		Direction[]  directions = Direction.values();
		
		int randonNum = new Random().nextInt(directions.length);
		return directions[randonNum];
		
	}
	
	public static Direction randonDirectionNoStop(){
		/**
		 * Direction.values()返回的是Directin的数组
		 */
		Direction[]  directions = Direction.values();
		
		int randonNum = new Random().nextInt(directions.length - 1);
		return directions[randonNum];
		
	}

}
