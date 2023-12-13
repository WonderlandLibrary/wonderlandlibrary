package summer.base.utilities;

public class ServerUtils {
	
	public static enum Server {
		Hypixel,
		Mineplex,
		Hive,
		Cubecraft,
		Unknown,
	}
	
	public static Server getCurrentServer() {
		if(mc.getCurrentServerData() != null) {
			String serverIP = mc.getCurrentServerData().serverIP.toLowerCase();
			
			if(serverIP.contains("hypixel")) return Server.Hypixel;
			if(serverIP.contains("mineplex")) return Server.Mineplex;
			if(serverIP.contains("hivemc")) return Server.Hive;
			if(serverIP.contains("cubecraft")) return Server.Cubecraft;
			return Server.Unknown;
		}
		return Server.Unknown;
	}

	
    
}
