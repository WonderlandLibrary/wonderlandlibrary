package dev.tenacity.util.render;

import dev.tenacity.module.ModuleCategory;
import dev.tenacity.module.Module;
import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.setting.impl.NumberSetting;

public class NotificationsMod extends Module {

    public static final NumberSetting colorInterpolation = new NumberSetting("Color Value", .5, 1, 0, .05);
    public static final BooleanSetting toggleNotifications = new BooleanSetting("Toggle", true);

    public NotificationsMod() {
        super("Notifications", "Allows you to customize the client notifications", ModuleCategory.RENDER);
        initializeSettings(colorInterpolation, toggleNotifications);
    }

}
