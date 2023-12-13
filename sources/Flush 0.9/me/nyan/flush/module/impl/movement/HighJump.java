package me.nyan.flush.module.impl.movement;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.movement.MovementUtils;

public class HighJump extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "Verus", "Redesky");
    private final NumberSetting height = new NumberSetting("Height", this, 3, 1, 10, 0.1);

    private boolean veru;

    public HighJump() {
        super("HighJump", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        veru = true;

        Flush.disableVerusDisabler();
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        switch (mode.getValue().toUpperCase()) {
            case "VANILLA":
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = height.getValue();
                } else {
                    disable();
                }
                break;

            case "REDESKY":
                if (mc.thePlayer.onGround) {
                    MovementUtils.vClip(height.getValue() + 2.36872);
                    MovementUtils.packetvClip(-1.56872);

                    MovementUtils.packethClip(19);
                } else {
                    disable();
                }

                mc.thePlayer.motionY = 0;
                break;
            case "VERUS":
                Flush.enableVerusDisabler();

                if (veru) {
                    mc.thePlayer.motionY = height.getValue();
                    veru = false;
                } else {
                    if (mc.thePlayer.motionY < 0) {
                        disable();
                        Flush.disableVerusDisabler();
                    }
                }
                break;
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}
