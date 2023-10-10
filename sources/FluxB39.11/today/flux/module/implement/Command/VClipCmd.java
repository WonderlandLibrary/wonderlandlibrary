package today.flux.module.implement.Command;

import today.flux.module.Command;
import today.flux.utility.MathUtils;

@Command.Info(name = "vclip", syntax = { "<height>" }, help = "Teleport in the vertical direction")
public class VClipCmd extends Command {
	@Override
	public void execute(String[] args) throws Error {
		if (args.length > 1) {
			this.syntaxError();
		} else if (args.length == 1) {
			if (MathUtils.isDouble(args[0])) {
				this.mc.thePlayer.setPosition(this.mc.thePlayer.posX,
						this.mc.thePlayer.posY + Double.parseDouble(args[0]), this.mc.thePlayer.posZ);
			} else {
				this.syntaxError();
			}
		} else {
			this.syntaxError();
		}
	}
}
