package today.flux.module.implement.Command;

import today.flux.Flux;
import today.flux.module.Command;
import today.flux.module.Module;
import today.flux.utility.ChatUtils;

@Command.Info(name = "t", syntax = { "<Module>" }, help = "Toggle specified module.")
public class ToggleCmd extends Command {

	@Override
	public void execute(String[] args) throws Error {
		if (args.length < 1) {
			this.syntaxError();
		} else {
			Module mod = Flux.INSTANCE.getModuleManager().getModuleByName(args[0]);
			if (mod != null) {
				mod.toggle();
			} else {
				ChatUtils.sendMessageToPlayer("Invalid module (" + args[0] + ")");
			}
		}
	}

}
