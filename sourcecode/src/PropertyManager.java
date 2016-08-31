import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
	
	private static Properties prop = null;
	
	static{
		prop = new Properties();

		try {
			prop.load(PropertyManager.class.getClassLoader().getResourceAsStream("properties/TankWar.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		;
	}
	
	private PropertyManager(){};
	
	public static String getProperty(String key){
		return prop.getProperty(key);
	}
	
	/*public static Object getProperty(Object key){
		return prop.get(key);
	}*/
	
}
