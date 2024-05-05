/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockSlime
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockSlime;

@ModuleInfo(name="SlimeJump", description="Allows you to to jump higher on slime blocks.", category=ModuleCategory.MOVEMENT)
public class SlimeJump
extends Module {
    private final FloatValue motionValue = new FloatValue("Motion", 0.42f, 0.2f, 1.0f);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Set", "Add"}, "Add");

    @EventTarget
    public void onJump(JumpEvent event) {
        if (SlimeJump.mc.field_71439_g != null && SlimeJump.mc.field_71441_e != null && BlockUtils.getBlock(SlimeJump.mc.field_71439_g.func_180425_c().func_177977_b()) instanceof BlockSlime) {
            event.cancelEvent();
            switch (((String)this.modeValue.get()).toLowerCase()) {
                case "set": {
                    SlimeJump.mc.field_71439_g.field_70181_x = ((Float)this.motionValue.get()).floatValue();
                    break;
                }
                case "add": {
                    SlimeJump.mc.field_71439_g.field_70181_x += (double)((Float)this.motionValue.get()).floatValue();
                }
            }
        }
    }
}

