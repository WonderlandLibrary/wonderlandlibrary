package vestige.command.impl;

import vestige.Vestige;
import vestige.api.module.Module;
import vestige.command.Command;
import vestige.util.base.IMinecraft;

public class VClip extends Command implements IMinecraft {
	
	public VClip() {
		super("VClip", "Clips vertically", "vclip <y pos>");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			double y = Double.parseDouble(args[0]);
			
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + y, mc.thePlayer.posZ);
		}
	}

}
