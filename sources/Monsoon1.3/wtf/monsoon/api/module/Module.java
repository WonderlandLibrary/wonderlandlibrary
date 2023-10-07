package wtf.monsoon.api.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.Wrapper;
import wtf.monsoon.api.setting.Setting;
import wtf.monsoon.impl.ui.notification.Notification;
import wtf.monsoon.impl.ui.notification.NotificationManager;
import wtf.monsoon.impl.ui.notification.NotificationType;
import net.minecraft.util.EnumChatFormatting;

public class Module implements Wrapper {
	
	public Category category;
	
	public String name;
	public String suffix;
	public String description;
	
	public int key;
	
	public boolean enabled;
	public boolean visible;
	private boolean open;
	public boolean disableOnLagback;
	
	public List<Setting> settings = new ArrayList<Setting>();
	
	public Module(String name, String description, int keybind, Category category) {
		this.name = name;
		this.description = description;
		this.key = keybind;
		this.category = category;
	}
	
	public void addSettings(Setting... settings) {
		this.settings.addAll(Arrays.asList(settings));
	}
	
	public void toggle()
	{
		this.setEnabled(!enabled);
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		
		if (enabled)
		{
			onEnable();
		}
		
		if (!enabled)
		{
			onDisable();
		}
	}
	
	public void setEnabledSilent(boolean enabled) {
		this.enabled = enabled;
		
		if (enabled)
		{
			Monsoon.INSTANCE.eventManager.register(this);
		}
		
		if (!enabled)
		{
			Monsoon.INSTANCE.eventManager.unregister(this);
		}
	}
	
	public void onEnable() {
		if(Monsoon.INSTANCE.manager.notifs.modToggle.isEnabled() && name != "Blink" && name != "ClickGUI") {
			NotificationManager.show(new Notification(NotificationType.SUCCESS, name, name + " was enabled.", 1));
		}
		Monsoon.INSTANCE.eventManager.register(this);
	}
	
	public void onDisable() {
		if(Monsoon.INSTANCE.manager.notifs.modToggle.isEnabled() && name != "Blink" && name != "ClickGUI") {
			NotificationManager.show(new Notification(NotificationType.FAIL, name, name + " was disabled.", 1));
		}
		Monsoon.INSTANCE.eventManager.unregister(this);
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public int getKey()
	{
		return key;
	}
	
	public void setKey(int keybind) {
		this.key = keybind;
	}
	
	public Category getCategory()
	{
		return category;
	}
	
	public boolean isVisible()
	{
		return visible;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public String getDisplayname() {
		if(suffix != null) {
			return name + EnumChatFormatting.GRAY + " " + suffix;
		} else return name;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public void setOpen(boolean open)
	{
		this.open = open;
	}

	public boolean isOpen()
	{
		return open;
	}
	
}
