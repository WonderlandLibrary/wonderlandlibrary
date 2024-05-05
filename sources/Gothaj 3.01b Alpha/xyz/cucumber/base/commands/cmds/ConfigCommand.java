package xyz.cucumber.base.commands.cmds;

import java.io.File;

import xyz.cucumber.base.Client;
import xyz.cucumber.base.commands.Command;
import xyz.cucumber.base.commands.CommandInfo;
import xyz.cucumber.base.utils.FileUtils;
import xyz.cucumber.base.utils.cfgs.ConfigFileUtils;

@CommandInfo(aliases = { "cfg", "c", "config" }, name = "Config", usage = ".cfg <load/save/remove/list> <File Name>")
public class ConfigCommand extends Command {

	public FileUtils file = new FileUtils("Gothaj/configs", "");

	@Override
	public void onSendCommand(String[] args) {

		if (args.length == 0) {
			this.sendUsage();
			return;
		}

		if (!args[0].equalsIgnoreCase("list")) {
			if (args.length != 2) {
				this.sendUsage();
				return;
			}
		}

		if (args[0].toLowerCase().equals("load")) {
			ConfigFileUtils.save(file.getFile(), false);

			file.setFile(new File(file.getDirectory(), args[1] + ".json"));

			if (!file.getFile().exists()) {
				Client.INSTANCE.getCommandManager().sendChatMessage("§cSorry, but this config does not exists!");
				return;
			}
			ConfigFileUtils.load(file.getFile(), true, false);
			Client.INSTANCE.getCommandManager().sendChatMessage("§aConfig was successfully loaded!");

		} else if (args[0].toLowerCase().equals("loadOnline")) {
			
		} else if (args[0].toLowerCase().equals("save")) {
			ConfigFileUtils.save(file.getFile(), true);
			file.setFile(new File(file.getDirectory(), args[1] + ".json"));

			if (!file.getFile().exists()) {
				Client.INSTANCE.getCommandManager().sendChatMessage("§aConfig was successfully created!");
			} else {
				Client.INSTANCE.getCommandManager().sendChatMessage("§aConfig was saved!");
			}

			ConfigFileUtils.save(file.getFile(), true);

		} else if (args[0].toLowerCase().equals("remove")) {
			file.setFile(new File(file.getDirectory(), args[1] + ".json"));

			if (!file.getFile().exists()) {
				Client.INSTANCE.getCommandManager().sendChatMessage("§cSorry, but this config does not exists!");
				return;
			}

			file.getFile().delete();
			Client.INSTANCE.getCommandManager().sendChatMessage("§aConfig was successfully deleted!");
		} else if (args[0].toLowerCase().equals("list")) {
			for (File f : file.getDirectory().listFiles()) {
				if(!f.getName().endsWith(".json")) continue;
				Client.INSTANCE.getCommandManager().sendChatMessage("§a" + f.getName().replace(".json", "") + "");
			}
		} else {
			this.sendUsage();
			return;
		}
	}
}
