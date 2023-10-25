package club.async.module.impl.movement;

import club.async.event.impl.EventMovePlayer;
import club.async.event.impl.EventUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.ModeSetting;
import club.async.module.setting.impl.NumberSetting;
import club.async.util.MovementUtil;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "Flight", description = "Fly", category = Category.MOVEMENT)
public class Flight extends Module {

    public ModeSetting mode = new ModeSetting("Mode", this, new String[]{"Vanilla"}, "Vanilla");
    public NumberSetting vanillaSpeed = new NumberSetting("Speed", this, 0.3, 5, 1, 0.1, () -> mode.getCurrMode().equalsIgnoreCase("Vanilla"));

    @Handler
    public final void movePlayer(EventMovePlayer event) {
        switch (mode.getCurrMode())
        {
            case "Vanilla":

                break;
        }
    }

    @Handler
    public final void update(EventUpdate eventUpdate) {
        setExtraTag(mode.getCurrMode());
        switch (mode.getCurrMode())
        {
            case "Vanilla":
//                vanilla();
                break;
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        MovementUtil.verusDMG ();
    }
    @Override
    public void onDisable() {
        super.onDisable();
        if(mode.getCurrMode().equalsIgnoreCase("vanilla")) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionY = 0;
            mc.thePlayer.motionZ = 0;
        }
    }



}
