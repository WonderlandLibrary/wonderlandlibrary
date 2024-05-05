/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockPane
 *  net.minecraft.util.BlockPos
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockPane;
import net.minecraft.util.BlockPos;

@ModuleInfo(name="HighJump", description="Allows you to jump higher.", category=ModuleCategory.MOVEMENT)
public class HighJump
extends Module {
    private final FloatValue heightValue = new FloatValue("Height", 2.0f, 1.1f, 5.0f);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Damage", "AACv3", "DAC", "Mineplex"}, "Vanilla");
    private final BoolValue glassValue = new BoolValue("OnlyGlassPane", false);

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (((Boolean)this.glassValue.get()).booleanValue() && !(BlockUtils.getBlock(new BlockPos(HighJump.mc.field_71439_g.field_70165_t, HighJump.mc.field_71439_g.field_70163_u, HighJump.mc.field_71439_g.field_70161_v)) instanceof BlockPane)) {
            return;
        }
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "damage": {
                if (HighJump.mc.field_71439_g.field_70737_aN <= 0 || !HighJump.mc.field_71439_g.field_70122_E) break;
                HighJump.mc.field_71439_g.field_70181_x += (double)(0.42f * ((Float)this.heightValue.get()).floatValue());
                break;
            }
            case "aacv3": {
                if (HighJump.mc.field_71439_g.field_70122_E) break;
                HighJump.mc.field_71439_g.field_70181_x += 0.059;
                break;
            }
            case "dac": {
                if (HighJump.mc.field_71439_g.field_70122_E) break;
                HighJump.mc.field_71439_g.field_70181_x += 0.049999;
                break;
            }
            case "mineplex": {
                if (HighJump.mc.field_71439_g.field_70122_E) break;
                MovementUtils.strafe(0.35f);
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (((Boolean)this.glassValue.get()).booleanValue() && !(BlockUtils.getBlock(new BlockPos(HighJump.mc.field_71439_g.field_70165_t, HighJump.mc.field_71439_g.field_70163_u, HighJump.mc.field_71439_g.field_70161_v)) instanceof BlockPane)) {
            return;
        }
        if (!HighJump.mc.field_71439_g.field_70122_E && "mineplex".equals(((String)this.modeValue.get()).toLowerCase())) {
            HighJump.mc.field_71439_g.field_70181_x = HighJump.mc.field_71439_g.field_70181_x + (HighJump.mc.field_71439_g.field_70143_R == 0.0f ? 0.0499 : 0.05);
        }
    }

    @EventTarget
    public void onJump(JumpEvent event) {
        if (((Boolean)this.glassValue.get()).booleanValue() && !(BlockUtils.getBlock(new BlockPos(HighJump.mc.field_71439_g.field_70165_t, HighJump.mc.field_71439_g.field_70163_u, HighJump.mc.field_71439_g.field_70161_v)) instanceof BlockPane)) {
            return;
        }
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "vanilla": {
                event.setMotion(event.getMotion() * ((Float)this.heightValue.get()).floatValue());
                break;
            }
            case "mineplex": {
                event.setMotion(0.47f);
            }
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

