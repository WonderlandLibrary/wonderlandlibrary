package epsilon.command.impl;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.command.Command;
import epsilon.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;


public class Vclip extends Command {

	public Vclip() {
		super("Vclip", "Vclip", "clip", "v");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if (args.length > 0) {
            final double dist = Double.parseDouble(args[0]);
            
            Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY+ dist , Minecraft.getMinecraft().thePlayer.posZ);
		}
	}
	
}
