package markgg.command.impl;

import markgg.RazeClient;
import markgg.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumChatFormatting;

public class SetYaw extends Command {

	private final Minecraft mc = Minecraft.getMinecraft();
	
	public SetYaw() {
        super("SetYaw", "Set your yaw", "setyaw <yaw>", "sy");
    }
	
	@Override
	public void onCommand(String[] args, String command) {
		float yaw = 0;
		try {
			yaw = Float.parseFloat(command.split(" ")[1]);
		} catch (Exception e) {
			RazeClient.addChatMessage("Error getting number: " + EnumChatFormatting.RED + "please enter a valid number!");
			return;
		}
		if (yaw >= -360 && yaw <= 360 && yaw != Float.NaN) {
			mc.thePlayer.rotationYaw = yaw;
			RazeClient.addChatMessage("Set your yaw to " + EnumChatFormatting.GREEN + yaw);
		}else
			RazeClient.addChatMessage("Please enter a number between -360 and 360!");
	}
	
}
