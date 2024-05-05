package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.Level;
import me.aidanmees.trivia.gui.Notification;
import me.aidanmees.trivia.module.Module;

public class CommandToggle extends Command {

	@Override
	public void run(String[] commands) {
		String name = "";
		for (int i = 0; i < commands.length; i++) {
			if (i == 0) {
				continue;
			}
			name += commands[i];
			name += " ";
		}
		name = name.trim();
		Module module = trivia.getModuleByName(name);
		if (module == null) {
			trivia.getNotificationManager()
			.addNotification(new Notification(Level.ERROR, "Could not find module \"" + name + "\"!"));
			return;
		}
		module.toggle();

		if (module.isToggled()) {
			if (!(ClientSettings.notificationModulesEnable && trivia.getModuleByName("Notifications").isToggled())) {
				trivia.getNotificationManager()
						.addNotification(new Notification(Level.INFO, "Module " + module.getName() + " was enabled!"));
			}

		} else {
			if (!(ClientSettings.notificationModulesDisable && trivia.getModuleByName("Notifications").isToggled())) {
				trivia.getNotificationManager()
						.addNotification(new Notification(Level.INFO, "Module " + module.getName() + " was disabled!"));
			}
		}
	}

	@Override
	public String getActivator() {
		return ".t";
	}

	@Override
	public String getSyntax() {
		return ".t <module>";
	}

	@Override
	public String getDesc() {
		return "Toggles a module";
	}
}
