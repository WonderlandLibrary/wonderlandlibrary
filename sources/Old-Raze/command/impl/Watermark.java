package markgg.command.impl;

import org.lwjgl.opengl.Display;

import markgg.RazeClient;
import markgg.command.Command;
import markgg.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class Watermark extends Command {

	private final Minecraft mc = Minecraft.getMinecraft();

	public Watermark() {
		super("Watermark", "Sets the client's watermark", "watermark <name> | watermark reset", "wm");
	}

	@Override
	public void onCommand(String[] args, String command) {
	    if (args.length > 0 && !args[0].equalsIgnoreCase("reset")) {
	        String wName = String.join(" ", args);

	        Display.setTitle(wName);
	        RazeClient.addChatMessage("Set the watermark to " + EnumChatFormatting.GREEN + wName);
	    } else if(args[0].equalsIgnoreCase("reset")) {
	    	Display.setTitle(RazeClient.INSTANCE.getName() + " " + RazeClient.INSTANCE.getVersion());
	    	RazeClient.addChatMessage("Reset the watermark to it's default state.");
	    } else 
	    	RazeClient.addChatMessage("Error setting watermark: " + EnumChatFormatting.RED + "not enough arguments");
	}

}
