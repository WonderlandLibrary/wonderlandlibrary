package vestige.util.network;

import vestige.util.base.IMinecraft;

public class ServerUtils implements IMinecraft {
	
	public static boolean isOnHypixel() {
		if(mc.getCurrentServerData() == null || mc.thePlayer == null)
			return false;
		
		return mc.getCurrentServerData().serverIP.toLowerCase().endsWith("hypixel.net");
	}
	
	public static boolean isOnBlocksmc() {
		if(mc.getCurrentServerData() == null || mc.thePlayer == null)
			return false;
		
		return mc.getCurrentServerData().serverIP.toLowerCase().endsWith("blocksmc.com");
	}
	
	public static boolean isOnRedesky() {
		if(mc.getCurrentServerData() == null || mc.thePlayer == null)
			return false;
		
		return mc.getCurrentServerData().serverIP.contains("redesky.gg") || mc.getCurrentServerData().serverIP.contains("redesky.com") || mc.getCurrentServerData().serverIP.contains("redesky.net");
	}

	public static boolean isOnFuncraft() {
		if(mc.getCurrentServerData() == null || mc.thePlayer == null)
			return false;

		return mc.getCurrentServerData().serverIP.contains("funcraft.net") || mc.getCurrentServerData().serverIP.contains("funcraft.fr");
	}

	public static boolean isOnHycraft() {
		if(mc.getCurrentServerData() == null || mc.thePlayer == null)
			return false;

		return mc.getCurrentServerData().serverIP.contains("play.hycraft.us");
	}
	
	public static boolean isOnSingleplayer() {
		return mc.isSingleplayer();
	}
	
	public static String getCurrentServer() {
		if(mc.isSingleplayer()) {
			return "Singleplayer";
		} else {
			return mc.getCurrentServerData().serverIP;
		}
	}
	
}