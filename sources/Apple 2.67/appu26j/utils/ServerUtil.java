package appu26j.utils;

import appu26j.interfaces.MinecraftInterface;

public class ServerUtil implements MinecraftInterface
{
    private static String favouriteServer = "Hypixel";
    
	public static boolean isPlayerOnHypixel()
	{
		if (mc.getCurrentServerData() == null)
		{
			return false;
		}
		
		String IP = mc.getCurrentServerData().serverIP;
		return IP.toLowerCase().endsWith("hypixel.net") || IP.toLowerCase().endsWith("hypixel.io");
	}
	
	public static boolean tntExplodesEarly()
    {
        if (mc.getCurrentServerData() == null)
        {
            return false;
        }
        
        String IP = mc.getCurrentServerData().serverIP;
        return IP.toLowerCase().endsWith("hypixel.net") || IP.toLowerCase().endsWith("hypixel.io") || IP.toLowerCase().endsWith("bedwarspractice.club");
    }
	
	public static void setFavouriteServer(String favouriteServer)
	{
	    ServerUtil.favouriteServer = favouriteServer;
	}
	
	public static String getFavouriteServer()
	{
	    return favouriteServer;
	}
	
	public static String getServerName(String IP)
	{
	    String[] servers = new String[]
	    {
	            "bedwarspractice.club: BW Practice",
                "blocksmc.com: BlocksMC",
                "jartexnetwork.com: Jartex Network",
                "jartex.fun: Jartex Network",
                "pvp.land: PvP Land"
	    };
	    
	    for (String server : servers)
	    {
	        String serverIP = server.split(": ")[0];
            String serverName = server.split(": ")[1];
            
            if (IP.toLowerCase().endsWith(serverIP))
            {
                return serverName;
            }
	    }
	    
	    String temporary = "";
	    
	    if (IP.contains("."))
	    {
	        String finalIP = IP.substring(0, IP.lastIndexOf("."));
	        String temp = finalIP.contains(".") ? finalIP.substring(IP.indexOf(".") + 1) : finalIP;
	        temporary = String.valueOf(temp.charAt(0)).toUpperCase() + temp.substring(1);
	    }
	    
	    else
	    {
	        temporary = String.valueOf(IP.charAt(0)).toUpperCase() + IP.substring(1);
	    }
	    
	    temporary = temporary.replace("Mc", "MC").replace("mc", "MC").replace("pvp", "PvP");
	    return temporary;
	}
}
