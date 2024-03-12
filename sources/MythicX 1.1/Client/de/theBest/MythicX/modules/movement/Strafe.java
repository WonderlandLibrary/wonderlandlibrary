package de.theBest.MythicX.modules.movement;

import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.events.EventUpdate;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import de.theBest.MythicX.utils.PlayerUtil;
import de.Hero.settings.Setting;
import eventapi.EventTarget;

import java.awt.*;

public class Strafe extends Module {

    public Strafe() {
        super("Strafe", Type.Movement, 0, Category.MOVEMENT, Color.orange, "Lets you strafe");
    }
    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.hurtTime > 0 && !MythicX.setmgr.getSettingByName("Ignore Hurttime").getValBoolean())
            return;

        PlayerUtil.strafe(PlayerUtil.getHorizontalMotion());

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void setup() {
        MythicX.setmgr.rSetting(new Setting("Ignore Hurttime", this, true));
    }
}
