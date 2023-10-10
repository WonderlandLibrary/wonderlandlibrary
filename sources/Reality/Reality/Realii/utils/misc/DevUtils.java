package Reality.Realii.utils.misc;

import java.util.Random;


public class DevUtils {
    private static boolean dev;
    private static String devName;

    public static String getDevName() {
        return devName;
    }

    public static void setDevName(String devName) {
        DevUtils.devName = devName;
        System.out.println("Hello " + DevUtils.devName + "!");
    }

    public static boolean isDev() {
        return dev;
    }

    public static void setDev(boolean dev) {
        DevUtils.dev = dev;
    }
    
   
    public static String lol(String str) {
    	char[] e = new char[str.toCharArray().length];
    	
    	for(int i = 0; i < str.toCharArray().length; i++) {
    		e[i] = (char) (str.toCharArray()[i] ^ new Random().nextInt(999));
    	}
    	
    	return new String(e);
    }
}
