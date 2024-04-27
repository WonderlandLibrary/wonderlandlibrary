/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockSlime
 */
package net.dev.important.modules.module.modules.movement;

import net.dev.important.event.EventTarget;
import net.dev.important.event.JumpEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.value.FloatValue;
import net.dev.important.value.ListValue;
import net.minecraft.block.BlockSlime;

@Info(name="SlimeJump", spacedName="Slime Jump", description="Allows you to to jump higher on slime blocks.", category=Category.MOVEMENT, cnName="\u7c98\u6db2\u5757\u9ad8\u8df3")
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

