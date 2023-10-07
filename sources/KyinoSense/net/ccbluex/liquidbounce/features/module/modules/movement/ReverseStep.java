/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.util.AxisAlignedBB
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;

@ModuleInfo(name="ReverseStep", description="Allows you to step down blocks faster.", category=ModuleCategory.MOVEMENT)
public class ReverseStep
extends Module {
    private final FloatValue motionValue = new FloatValue("Motion", 1.0f, 0.21f, 1.0f);
    private boolean jumped;

    @EventTarget(ignoreCondition=true)
    public void onUpdate(UpdateEvent event) {
        if (ReverseStep.mc.field_71439_g.field_70122_E) {
            this.jumped = false;
        }
        if (ReverseStep.mc.field_71439_g.field_70181_x > 0.0) {
            this.jumped = true;
        }
        if (!this.getState()) {
            return;
        }
        if (BlockUtils.collideBlock(ReverseStep.mc.field_71439_g.func_174813_aQ(), block -> block instanceof BlockLiquid) || BlockUtils.collideBlock(new AxisAlignedBB(ReverseStep.mc.field_71439_g.func_174813_aQ().field_72336_d, ReverseStep.mc.field_71439_g.func_174813_aQ().field_72337_e, ReverseStep.mc.field_71439_g.func_174813_aQ().field_72334_f, ReverseStep.mc.field_71439_g.func_174813_aQ().field_72340_a, ReverseStep.mc.field_71439_g.func_174813_aQ().field_72338_b - 0.01, ReverseStep.mc.field_71439_g.func_174813_aQ().field_72339_c), block -> block instanceof BlockLiquid)) {
            return;
        }
        if (!ReverseStep.mc.field_71474_y.field_74314_A.func_151470_d() && !ReverseStep.mc.field_71439_g.field_70122_E && !ReverseStep.mc.field_71439_g.field_71158_b.field_78901_c && ReverseStep.mc.field_71439_g.field_70181_x <= 0.0 && ReverseStep.mc.field_71439_g.field_70143_R <= 1.0f && !this.jumped) {
            ReverseStep.mc.field_71439_g.field_70181_x = -((Float)this.motionValue.get()).floatValue();
        }
    }

    @EventTarget(ignoreCondition=true)
    public void onJump(JumpEvent event) {
        this.jumped = true;
    }
}

