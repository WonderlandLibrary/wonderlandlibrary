package me.aidanmees.trivia.client.commands;

import java.util.ArrayList;

public class CommandManager {

	public static ArrayList<Command> commands = new ArrayList<Command>();

	public CommandManager() {
		commands.add(new CommandBind());
		commands.add(new CommandHelp());
		commands.add(new CommandKickall());
		commands.add(new CommandSay());
		commands.add(new CommandBugUp());
		commands.add(new CommandToggle());
		commands.add(new CommandVclip());
		//commands.add(new CommandBot());
		commands.add(new CommandHastebin());
		commands.add(new CommandDamage());
		commands.add(new CommandFakehacker());
		commands.add(new CommandCleanRam());
		commands.add(new CommandInvsee());
		commands.add(new CommandCommands());
		commands.add(new CommandUp());
		commands.add(new CommandBleach());
		commands.add(new CommandArmorStandRain());
		commands.add(new CommandTeleport());
		commands.add(new CommandGround());
		commands.add(new CommandJump());
		commands.add(new CommandGive());
		//commands.add(new CommandClickGuiSize());
		commands.add(new CommandBook());
		commands.add(new CommandDeathStick());
		commands.add(new CommandKillerpotion());
		commands.add(new CommandNBTViewer());
		commands.add(new CommandMotionStand());
		commands.add(new CommandCReativeCrasher2());
		commands.add(new CommandCrashStick());
		commands.add(new CommandHologram());
		commands.add(new CommandPlayerHead());
		commands.add(new CommandCrasher());
		commands.add(new CommandFirework());
		
		commands.add(new CommandFriend());
		commands.add(new CommandServerVersion());
		commands.add(new CommandFakeMessage());
	}

	public boolean onCommand(String activator, String[] commands) {
		for (Command cmd : this.commands) {
			if (cmd.getActivator().equals(activator)) {
				cmd.run(commands);
				return true;
			}
		}
		return false;
	}

}
