package markgg.command.impl;

import markgg.RazeClient;
import markgg.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumChatFormatting;

public class SetPitch extends Command {

	private final Minecraft mc = Minecraft.getMinecraft();
	
	public SetPitch() {
        super("SetPitch", "Set your pitch", "setpitch <pitch>", "sp");
    }
	
	@Override
	public void onCommand(String[] args, String command) {
		float pitch = 0;
		try {
			pitch = Float.parseFloat(command.split(" ")[1]);
		} catch (Exception e) {
			RazeClient.addChatMessage("Error getting number: " + EnumChatFormatting.RED + "please enter a valid number!");
			return;
		}
		if (pitch >= -90 && pitch <= 90 && pitch != Float.NaN) {
			mc.thePlayer.rotationPitch = pitch;
			RazeClient.addChatMessage("Set your pitch to " + EnumChatFormatting.GREEN + pitch);
		}else
			RazeClient.addChatMessage("Please enter a number between -90 and 90!");
	}
	
}
