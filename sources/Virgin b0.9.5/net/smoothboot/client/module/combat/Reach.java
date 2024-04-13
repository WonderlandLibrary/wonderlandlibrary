package net.smoothboot.client.module.combat;

import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.BooleanSetting;
import net.smoothboot.client.module.settings.NumberSetting;

public class Reach extends Mod {

    public NumberSetting reachDistance = new NumberSetting("Reach", 3.00, 4.00, 3, 0.01);
    public BooleanSetting reachSprint = new BooleanSetting("Sprint check", false);

    public Reach() {
        super("Reach", "", Category.Combat);
        addsettings(reachDistance, reachSprint);
    }

    public static float reachHack = 3.0F;

    public static double reachHack() {
        return reachHack;
    }

    @Override
    public void onTick(){
        if (!reachSprint.isEnabled())
            reachHack = reachDistance.getValueFloat();
        else if (reachSprint.isEnabled() && mc.player.isSprinting()) {
            reachHack = reachDistance.getValueFloat();
        }
        else if (reachSprint.isEnabled() && !mc.player.isSprinting()) {
            reachHack = 3.0F;
        }
        super.onTick();
    }

    @Override
    public void onDisable() {
        reachHack = 3.0F;
        super.onDisable();
    }

}