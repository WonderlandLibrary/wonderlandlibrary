package wtf.diablo.module.impl.Render;

import com.google.common.eventbus.Subscribe;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.settings.impl.NumberSetting;

public class Animations extends Module {
    public static ModeSetting mode = new ModeSetting("Mode", "Diablo", "Slide", "Exhi", "Swing");
    public static NumberSetting scale = new NumberSetting("Scale", 1, 0.05, 0.1, 2);
    public static NumberSetting speed = new NumberSetting("Speed", 1, 0.05, 0.05, 3);
    public Animations() {
        super("Animations", "Animate your weapon", Category.RENDER, ServerType.All);
        this.addSettings(mode, scale, speed);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        this.setSuffix(mode.getMode());
    }
}
