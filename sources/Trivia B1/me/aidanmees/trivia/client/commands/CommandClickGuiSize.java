package me.aidanmees.trivia.client.commands;

import java.awt.Font;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.darkstorm.minecraft.gui.theme.simple.SimpleTheme;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.Level;
import me.aidanmees.trivia.gui.Notification;

public class CommandClickGuiSize extends Command {

	@Override
	public void run(String[] commands) {
		if (commands.length == 1 || commands.length > 2) {
			trivia.chatMessage("§cEnter one numer!");
			return;
		}
		int size;
		try {
			size = Integer.parseInt(commands[1]);
		} catch (NumberFormatException e) {
			trivia.chatMessage("§cEnter a valid number!");
			return;
		}
//		((SimpleTheme) trivia.getGUIMananger().getTheme()).fontRenderer = new UnicodeFontRenderer(
//				new Font("Segue UI", Font.PLAIN, size));
		trivia.getNotificationManager().addNotification(new Notification(Level.INFO, "Fontsize was set to: " + size));
		ClientSettings.clickGuiFontSize = size;
		//trivia.getGUIMananger().reload();
	}

	@Override
	public String getActivator() {
		return ".fontsize";
	}

	@Override
	public String getSyntax() {
		return ".fontsize <size>";
	}

	@Override
	public String getDesc() {
		return "Sets the font size for the ClickGUI. Default is 13.";
	}
}
