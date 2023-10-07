package vestige.command.impl;

import vestige.Vestige;
import vestige.api.module.Module;
import vestige.command.Command;
import vestige.util.base.IMinecraft;
import vestige.util.movement.MovementUtils;

public class HClip extends Command implements IMinecraft {
	
	public HClip() {
		super("HClip", "Clips horizontally, in the direction you are looking at", "vclip <y pos>");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			double dist = Double.parseDouble(args[0]);
			MovementUtils.hclip(dist);
		}
	}

}
