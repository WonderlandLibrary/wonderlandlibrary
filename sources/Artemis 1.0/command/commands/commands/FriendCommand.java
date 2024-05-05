package artemis.command.commands;

import org.lwjgl.input.Keyboard;

import artemis.Artemis;
import artemis.module;
import artemis.command.Command;
import net.minecraft.client.Minecraft;

public class FriendCommand extends Command{

	@Override
	public String getAlias() {
		return "friend";
	}

	@Override
	public String getDescription() {
		return "Allows user to add friends";
	}

	@Override
	public String getSyntax() {
		return ".friend add [Name] [Alias] | .friend del [Name] | .friend clear";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if(args[0].equalsIgnoreCase("add")){
			String name = args[1];
			String alias = args[2];
			if(!Artemis.instance.f.isFriend(name) && !name.equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getName())) {
				Artemis.instance.f.addFriend(name, alias);
				Artemis.addChatMessage("Added " + name + " as " + alias);
			}else {
				Artemis.addChatMessage(name + " is already your friend or is yourself");
			}
		}
		if(args[0].equalsIgnoreCase("del")) {
			String name = args[1];
			if(Artemis.instance.f.isFriend(name)) {
				Artemis.instance.f.removeFriend(name);
				Artemis.addChatMessage("Removed " + name + " from your friends list");
			}else {
				Artemis.addChatMessage(name + " is not your friend");
			}
		}
		if(args[0].equalsIgnoreCase("clear")) {
			Artemis.instance.f.clearFriends();
			Artemis.addChatMessage("Cleared friends");
		}
	}

}
