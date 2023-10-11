package markgg.command.impl;

import markgg.RazeClient;
import markgg.command.Command;
import markgg.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class VClip extends Command {

	private final Minecraft mc = Minecraft.getMinecraft();

	public VClip() {
		super("VClip", "Clip up or down.", "vclip <blocks>", "vc");
	}

	@Override
	public void onCommand(String[] args, String command) {
		double blocks = 0.0D;
		try {
			blocks = Double.parseDouble(command.split(" ")[1]);
		} catch (Exception e) {
			RazeClient.addChatMessage("Error getting number: " + EnumChatFormatting.RED + "please enter a valid number!");
			return;
		}
		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + blocks, mc.thePlayer.posZ);
		RazeClient.addChatMessage("Teleported you " + blocks + " blocks " + ((blocks < 0.0D) ? "down." : "up."));
	}

}
