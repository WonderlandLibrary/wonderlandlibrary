package me.nyan.flush.module.impl.movement;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.world.Scaffold;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import net.minecraft.potion.Potion;

public class Sprint extends Module {
    private final BooleanSetting omni = new BooleanSetting("Omni Sprint", this, false);

    public Sprint() {
        super("Sprint", Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (isEnabled(Scaffold.class) && !getModule(Scaffold.class).sprint.getValue()) {
            if (mc.thePlayer.isSprinting())
                mc.thePlayer.setSprinting(false);
            return;
        }
        if ((isEnabled(NoSlow.class) || !mc.thePlayer.isUsingItem()) &&
                (mc.thePlayer.getFoodStats().getFoodLevel() > 6 || mc.thePlayer.capabilities.isCreativeMode) && (omni.getValue() ? MovementUtils.isMoving() : mc.thePlayer.moveForward > 0)
                && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally && (!mc.thePlayer.isPotionActive(Potion.blindness))) {
            mc.thePlayer.setSprinting(true);
        }
    }
}