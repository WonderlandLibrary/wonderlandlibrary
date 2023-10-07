package wtf.monsoon.api;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Logger {
	
	public static void info(String msg) {
		System.out.println("<info> " + msg);
	}
	
	public static void warn(String msg) {
		LogManager.getLogger().warn("<WARNING> " + msg);
	}

}
