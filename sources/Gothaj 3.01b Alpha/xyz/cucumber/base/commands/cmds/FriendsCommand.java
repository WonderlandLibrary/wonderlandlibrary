package xyz.cucumber.base.commands.cmds;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.commands.Command;
import xyz.cucumber.base.commands.CommandInfo;
import xyz.cucumber.base.interf.KeybindGui;
import xyz.cucumber.base.module.Mod;

@CommandInfo(aliases = { "friends", "f" }, name = "Friends", usage = ".f <add / remove> <player name>")
public class FriendsCommand extends Command {

	public static CopyOnWriteArrayList<String> friends = new CopyOnWriteArrayList<String>();

	@Override
	public void onSendCommand(String[] args) {

		if (args.length != 2) {
			this.sendUsage();
			return;
		}
		
		if(args[1] == "") {
			this.sendUsage();
			return;
		}

		if (args[0].equalsIgnoreCase("add")) {
			if(friends.contains(args[1])) {
				Client.INSTANCE.getCommandManager().sendChatMessage(args[1] + " is already your friend");
			}else {
				friends.add(args[1]);
				Client.INSTANCE.getCommandManager().sendChatMessage("Added friend " + args[1]);
			}
		} else if (args[0].equalsIgnoreCase("remove")) {
			friends.remove(args[1]);
			Client.INSTANCE.getCommandManager().sendChatMessage("Removed friend " + args[1]);
		}else {
			this.sendUsage();
			return;
		}
	}

}
