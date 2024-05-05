package sudo.module.render;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.NumberSetting;
import sudo.utils.misc.Notification;
import sudo.utils.misc.NotificationUtil;

public class Notifications  extends Mod{
	
	public BooleanSetting enabled = new BooleanSetting("Enabled", false);
	public NumberSetting maxNotif = new NumberSetting("Notifications", 1,15,7,1);
	public NumberSetting roundness = new NumberSetting("Roundness", 0,8,7,1);
	
	public Notifications() {
		super("Notifications", "Shows notifications", Category.RENDER, 0);
		addSettings(enabled, maxNotif);
	}

	@Override
	public void onEnable() {
		NotificationUtil.send_notification(new Notification("Example notification", 202, 122, 248));
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
}
