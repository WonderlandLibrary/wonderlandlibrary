/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockLiquid
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.block.BlockLiquid;

@ModuleInfo(name="WaterSpeed", description="Allows you to swim faster.", category=ModuleCategory.MOVEMENT)
public class WaterSpeed
extends Module {
    private final FloatValue speedValue = new FloatValue("Speed", 1.2f, 1.1f, 1.5f);

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (WaterSpeed.mc.field_71439_g.func_70090_H() && BlockUtils.getBlock(WaterSpeed.mc.field_71439_g.func_180425_c()) instanceof BlockLiquid) {
            float speed = ((Float)this.speedValue.get()).floatValue();
            WaterSpeed.mc.field_71439_g.field_70159_w *= (double)speed;
            WaterSpeed.mc.field_71439_g.field_70179_y *= (double)speed;
        }
    }
}

