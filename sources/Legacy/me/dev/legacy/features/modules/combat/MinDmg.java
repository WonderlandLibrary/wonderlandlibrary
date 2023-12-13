package me.dev.legacy.features.modules.combat;

import com.google.common.eventbus.Subscribe;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;

public class MinDmg extends Module {
    public MinDmg() {
        super("MinDmg", "Set minimal damage for auto crystal.", Module.Category.COMBAT, true, false, false); INSTANCE = this;
    }
    private static MinDmg INSTANCE = new MinDmg();

    private final Setting<Float> EnableDamage = this.register(new Setting<Float>("Enable MinDmg", Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(36.0f)));
    private final Setting<Float> DisableDamage = this.register(new Setting<Float>("Disable MinDmg", Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(36.0f)));

    public static MinDmg getInstance() {
        return INSTANCE;
    }

    @Subscribe
    public void onEnable(){
        AutoCrystal.getInstance().minDamage.setValue(EnableDamage.getValue());
    }

    @Subscribe
    public void onDisable(){
        AutoCrystal.getInstance().minDamage.setValue(DisableDamage.getValue());
    }
}
