package xyz.cucumber.base.commands.cmds;

import xyz.cucumber.base.Client;
import xyz.cucumber.base.commands.Command;
import xyz.cucumber.base.commands.CommandInfo;
import xyz.cucumber.base.module.Mod;

@CommandInfo(aliases = { "toggle", "t", "tog" }, name = "Toggle", usage = ".t <Module name>")
public class ToggleCommand extends Command {

	@Override
	public void onSendCommand(String[] args) {
		
		if(args.length != 1)  {
			sendUsage();
			return;
		}
		
		Mod module = Client.INSTANCE.getModuleManager().getModule(args[0]);
		
		if(module == null)  {
			Client.INSTANCE.getCommandManager().sendChatMessage("§cSorry, but this Module does not exist!");
			return;
		}
		
		module.toggle();
		if(module.isEnabled())
			Client.INSTANCE.getCommandManager().sendChatMessage("§a"+ module.getName() + " §7was §aenabled§7!");
		else Client.INSTANCE.getCommandManager().sendChatMessage("§c"+ module.getName() + " §7was §cdisabled§7!");
	}
}
