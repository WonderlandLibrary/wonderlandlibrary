/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.potion.Potion
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.potion.Potion;

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
                if (!MovementUtils.isMoving() || Sprint.mc.field_71439_g.func_70093_af() || ((Boolean)this.blindnessValue.get()).booleanValue() && Sprint.mc.field_71439_g.func_70644_a(Potion.field_76440_q) || ((Boolean)this.foodValue.get()).booleanValue() && !((float)Sprint.mc.field_71439_g.func_71024_bL().func_75116_a() > 6.0f) && !Sprint.mc.field_71439_g.field_71075_bZ.field_75101_c) break block4;
                if (!((Boolean)this.checkServerSide.get()).booleanValue() || !Sprint.mc.field_71439_g.field_70122_E && ((Boolean)this.checkServerSideGround.get()).booleanValue() || ((Boolean)this.allDirectionsValue.get()).booleanValue() || RotationUtils.targetRotation == null) break block5;
                Rotation rotation = new Rotation(Sprint.mc.field_71439_g.field_70177_z, Sprint.mc.field_71439_g.field_70125_A);
                if (!(RotationUtils.getRotationDifference(rotation) > 30.0)) break block5;
            }
            Sprint.mc.field_71439_g.func_70031_b(false);
            return;
        }
        if (((Boolean)this.allDirectionsValue.get()).booleanValue() || Sprint.mc.field_71439_g.field_71158_b.field_78900_b >= 0.8f) {
            Sprint.mc.field_71439_g.func_70031_b(true);
        }
    }
}

