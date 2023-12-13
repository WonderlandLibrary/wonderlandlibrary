package dev.monsoon.module.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import dev.monsoon.Monsoon;
import dev.monsoon.event.Event;
import dev.monsoon.module.enums.Category;
import dev.monsoon.module.setting.Setting;
import dev.monsoon.notification.Notification;
import dev.monsoon.notification.NotificationManager;
import dev.monsoon.notification.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class Module {

	public String name, displayname, suffix;
	public int key;
	public Category category;
	public boolean disableOnLagback;
	public Minecraft mc = Minecraft.getMinecraft();
	public boolean enabled;
	public boolean expanded;
	public int index;
	public List<Setting> settings = new ArrayList<Setting>();

	private boolean open;
	
	public Module(String name, int key, Category c) {
		this.name = name;
		this.key = key;
		this.category = c;
	}
	
	public void setKey(int key) {
		this.key = key;
	}

	public void addSettings(Setting... settings) {
		this.settings.addAll(Arrays.asList(settings));
		//this.settings.sort(Comparator.comparingInt(s -> s == keyCode & 1 : 0));
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public Setting getSettingByName(Module mod, String name) {
		for (Setting set : mod.settings) {
			if (set.name.equalsIgnoreCase(name) && set.parent == mod) {
				return set;
			}
		}
		return null;
	}
	
	public void setEnabled(boolean object) {
		this.enabled = object;
		//Config.save();
		if(mc.theWorld != null) {
			if(object) {
				onEnable();
				if(Monsoon.manager.notifs.modToggle.isEnabled() && name != "Blink" && name != "ClickGUI") {
					NotificationManager.show(new Notification(NotificationType.SUCCESS, name, name + " was enabled.", 1));
				}
			} else {
				onDisable();
				if(Monsoon.manager.notifs.modToggle.isEnabled() && name != "Blink" && name != "ClickGUI") {
					NotificationManager.show(new Notification(NotificationType.FAIL, name, name + " was disabled.", 1));
				}
			}
		}

		Thread saveThread = new Thread() {
			public void run() {
				if(Monsoon.saveLoad != null) {
					Monsoon.saveLoad.save();
				}
			}
		};
		saveThread.start();
	}

	public void setOpen(boolean open)
	{
		this.open = open;
	}

	public boolean isOpen()
	{
		return open;
	}
	
	public void setEnabledSilent(Object object) {
		this.enabled = (boolean) object;
	}
	
	public int getKey() {
		return key;
	}

	public void onEvent(Event e) {
		
	}
	
	public void toggle() {
		enabled = !enabled;
		//Config.save();
		if(mc.theWorld != null) {
			if(enabled) {
	            onEnable();
				if(Monsoon.manager.notifs.modToggle.isEnabled() && name != "Blink" && name != "ClickGUI") {
					NotificationManager.show(new Notification(NotificationType.SUCCESS, name, name + " was enabled.", 1));
				}
	        } else {
	            onDisable();
				if(Monsoon.manager.notifs.modToggle.isEnabled() && name != "Blink" && name != "ClickGUI") {
					NotificationManager.show(new Notification(NotificationType.FAIL, name, name + " was disabled.", 1));
				}
			}
		}

		Thread saveThread = new Thread() {
			public void run() {
				if(Monsoon.saveLoad != null) {
					Monsoon.saveLoad.save();
				}
			}
		};
		saveThread.start();
	}
	
	public void onEnable() {

	}
	
	public void onDisable() {

	}
	
	public String getDisplayname() {
		if(suffix != null) {
			if (Monsoon.arraylist.arrayPos.is("Top Right")) {
				return name + EnumChatFormatting.GRAY + " " + suffix;
			} else {
				return EnumChatFormatting.GRAY + suffix + EnumChatFormatting.RESET + " " + name;
			}
		} else return name;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}



	public String getName() {
		if(Monsoon.arraylist.fontMode.is("Lowercase")) {
			return name.toLowerCase();
		} else if(Monsoon.arraylist.fontMode.is("Impactful")) {
			return ChatFormatting.BOLD + name.toUpperCase();
		}else { //Normal
			return name;
		}
	}

	public void toggleSilent() {
		enabled = !enabled;
		if(enabled) {
            onEnable();
        } else {
            onDisable();
		}
	}
	
}
