package me.crimp.claudius.mod.modules.render;

import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;

public class SwingSpeed extends Module {
    public Setting<Integer> sped = register(new Setting<>("Speed", 20, 0, 40));
    public SwingSpeed() {
        super("SwingSpeed", "Changes Swing Speed", Category.Render, false, false);
    }
    public static SwingSpeed INSTANCE = new SwingSpeed();
}
