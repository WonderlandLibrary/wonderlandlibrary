/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="Sprint", description="Automatically sprints all the time.", category=ModuleCategory.MOVEMENT)
public class Sprint
extends Module {
    public final BoolValue allDirectionsValue = new BoolValue("AllDirections", true);
    public final BoolValue blindnessValue = new BoolValue("Blindness", true);
    public final BoolValue foodValue = new BoolValue("Food", true);
    public final BoolValue checkServerSide = new BoolValue("CheckServerSide", false);
    public final BoolValue checkServerSideGround = new BoolValue("CheckServerSideOnlyGround", false);

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        block5: {
            block4: {
                if (!MovementUtils.isMoving() || mc.getThePlayer().isSneaking() || ((Boolean)this.blindnessValue.get()).booleanValue() && mc.getThePlayer().isPotionActive(classProvider.getPotionEnum(PotionType.BLINDNESS)) || ((Boolean)this.foodValue.get()).booleanValue() && !((float)mc.getThePlayer().getFoodStats().getFoodLevel() > 6.0f) && !mc.getThePlayer().getCapabilities().getAllowFlying()) break block4;
                if (!((Boolean)this.checkServerSide.get()).booleanValue() || !mc.getThePlayer().getOnGround() && ((Boolean)this.checkServerSideGround.get()).booleanValue() || ((Boolean)this.allDirectionsValue.get()).booleanValue() || RotationUtils.targetRotation == null) break block5;
                Rotation rotation = new Rotation(mc.getThePlayer().getRotationYaw(), mc.getThePlayer().getRotationPitch());
                if (!(RotationUtils.getRotationDifference(rotation) > 30.0)) break block5;
            }
            mc.getThePlayer().setSprinting(false);
            return;
        }
        if (((Boolean)this.allDirectionsValue.get()).booleanValue() || mc.getThePlayer().getMovementInput().getMoveForward() >= 0.8f) {
            mc.getThePlayer().setSprinting(true);
        }
    }
}

