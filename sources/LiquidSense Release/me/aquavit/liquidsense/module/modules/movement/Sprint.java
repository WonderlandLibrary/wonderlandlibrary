package me.aquavit.liquidsense.module.modules.movement;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.utils.client.Rotation;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.value.BoolValue;
import net.minecraft.potion.Potion;

@ModuleInfo(name = "Sprint", description = "Automatically sprints all the time.", category = ModuleCategory.MOVEMENT)
public class Sprint extends Module {

    public final BoolValue allDirectionsValue = new BoolValue("AllDirections", true);
    public final BoolValue blindnessValue = new BoolValue("Blindness", true);
    public final BoolValue foodValue = new BoolValue("Food", true);

    public final BoolValue checkServerSide = new BoolValue("CheckServerSide", false);
    public final BoolValue checkServerSideGround = new BoolValue("CheckServerSideOnlyGround", false);

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if(!MovementUtils.isMoving() || mc.thePlayer.isSneaking() || (blindnessValue.get() && mc.thePlayer.isPotionActive(Potion.blindness))|| (foodValue.get() && !(mc.thePlayer.getFoodStats().getFoodLevel() > 6.0F || mc.thePlayer.capabilities.allowFlying)) || (checkServerSide.get() && (mc.thePlayer.onGround || !checkServerSideGround.get()) && !allDirectionsValue.get() && RotationUtils.targetRotation != null && RotationUtils.getRotationDifference(new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)) > 30)) {
            mc.thePlayer.setSprinting(false);
            return;
        }

        if (allDirectionsValue.get() || mc.thePlayer.movementInput.moveForward >= 0.8F){
            mc.thePlayer.setSprinting(true);
        }
    }
}
