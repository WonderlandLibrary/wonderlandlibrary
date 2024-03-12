package xyz.cucumber.base.commands.cmds;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.commands.Command;
import xyz.cucumber.base.commands.CommandInfo;
import xyz.cucumber.base.interf.KeybindGui;
import xyz.cucumber.base.module.Mod;

@CommandInfo(aliases = { "bind", "b" }, name = "Bind", usage = ".b <Module name> <optional - keybind>")
public class BindCommand extends Command {

	@Override
	public void onSendCommand(String[] args) {

		if (args.length == 0 || args.length > 2) {
			this.sendUsage();
			return;
		}

		Mod module = Client.INSTANCE.getModuleManager().getModule(args[0]);

		if (module == null) {
			Client.INSTANCE.getCommandManager().sendChatMessage("§cSorry, but this Module does not exist!");
			return;
		}

		if (args.length == 1) {
			Minecraft.getMinecraft().displayGuiScreen(new KeybindGui(module));
		}

		if (args.length == 2) {
			module.setKey(Keyboard.getKeyIndex(args[1].toUpperCase()));
			Client.INSTANCE.getCommandManager().sendChatMessage("Module " + args[0].toUpperCase() + " was bound to " + args[1].toUpperCase());
		}
	}

}
