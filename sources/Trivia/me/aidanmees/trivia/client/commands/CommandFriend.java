package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.gui.Level;
import me.aidanmees.trivia.gui.Notification;

public class CommandFriend extends Command {

	@Override
	public void run(final String[] commands) {
		if (commands.length < 2) {
			trivia.chatMessage("§cEnter a player name!");
			return;
		}
		if (trivia.getFriendsMananger().isFriend(commands[1])) {
			trivia.getNotificationManager().addNotification(new Notification(Level.INFO, "Added " + commands[1] + " as a friend!"));
			trivia.getFriendsMananger().removeFriend(commands[1]);
		} else {
			trivia.getNotificationManager().addNotification(new Notification(Level.INFO, "Removed friend: " + commands[1] + "!"));
			trivia.getFriendsMananger().getFriends().add(commands[1]);
		}
	}

	@Override
	public String getActivator() {
		return ".friend";
	}

	@Override
	public String getSyntax() {
		return ".friend <player>";
	}

	@Override
	public String getDesc() {
		return "Adds a player as a friend";
	}
}
