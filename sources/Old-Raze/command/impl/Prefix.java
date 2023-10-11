package markgg.command.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.lwjgl.opengl.Display;

import markgg.RazeClient;
import markgg.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.EnumChatFormatting;

public class Prefix extends Command {

	public Prefix() {
		super("Prefix", "Change the command prefix", "prefix <char>", "p");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if (args.length > 0) {
			String wName = args[0].split(" ")[0];

			RazeClient.INSTANCE.getCmdmanager().prefix = wName;

			File cfgFile = new File("Raze/config.properties");
			try {
				if (!cfgFile.exists()) {
					cfgFile.createNewFile();
				}
				Properties properties = new Properties();
				OutputStream outputStream = new FileOutputStream("Raze/config.properties");
				properties.setProperty("prefix", wName);
				properties.store(outputStream, "Raze Properties");
				outputStream.close();
			} catch (IOException io) {
				RazeClient.addChatMessage("Error: " + EnumChatFormatting.RED + io.getMessage());
				io.printStackTrace();
			}

			RazeClient.addChatMessage("Set the prefix to " + EnumChatFormatting.GREEN + wName);
		} else
			RazeClient.addChatMessage("Error: " + EnumChatFormatting.RED + "character parameter cannot be empty!");
	}

}
