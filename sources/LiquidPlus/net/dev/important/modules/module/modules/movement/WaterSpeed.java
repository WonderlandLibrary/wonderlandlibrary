/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockLiquid
 */
package net.dev.important.modules.module.modules.movement;

import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.value.FloatValue;
import net.minecraft.block.BlockLiquid;

@Info(name="WaterSpeed", spacedName="Water Speed", description="Allows you to swim faster.", category=Category.MOVEMENT, cnName="\u6c34\u4e0a\u52a0\u901f")
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

