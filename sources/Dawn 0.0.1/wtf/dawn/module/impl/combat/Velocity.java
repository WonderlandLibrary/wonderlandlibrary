package wtf.dawn.module.impl.combat;

import org.lwjgl.input.Keyboard;
import wtf.dawn.module.Category;
import wtf.dawn.module.ModuleInfo;
import wtf.dawn.notifications.Notification;
import wtf.dawn.notifications.NotificationManager;
import wtf.dawn.notifications.NotificationType;
import wtf.dawn.module.Module;
import wtf.dawn.settings.impl.ModeSetting;
import wtf.dawn.settings.impl.SliderSetting;

import java.lang.String;

@ModuleInfo(getName = "Velocity", getCategory = Category.Combat, getDescription = "Reduces the knockback you take")
public class Velocity extends Module {

    public Velocity() {
        setKeyCode(Keyboard.KEY_L);
    }

    private final ModeSetting mode = new ModeSetting("Mode", "Packet", new String[]{"Packet", "Cancel", "Watchdog"});
    private final SliderSetting horizontalValue = new SliderSetting("Horizontal Value", 0.0, 100.0, 0.0, 1.0);
    private final SliderSetting verticalValue = new SliderSetting("Vertical Value", 0.0, 100.0, 0.0, 1.0);
    private final SliderSetting chance = new SliderSetting("Chance", 100.0, 100.0, 0.0, 1.0);

    @Override
    public void onDisable() {
        NotificationManager.show(new Notification(NotificationType.DISABLED, "Disabled!", "Velocity was disabled.", 1));
        super.onDisable();
    }

    @Override
    public void onEnable() {
        NotificationManager.show(new Notification(NotificationType.ENABLED, "Enabled!", "Velocity was enabled.", 1));
        super.onEnable();
    }

    {
    }
}
