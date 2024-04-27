/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockPane
 *  net.minecraft.util.BlockPos
 */
package net.dev.important.modules.module.modules.movement;

import net.dev.important.event.EventTarget;
import net.dev.important.event.JumpEvent;
import net.dev.important.event.MoveEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.util.BlockPos;

@Info(name="HighJump", spacedName="High Jump", description="Allows you to jump higher.", category=Category.MOVEMENT, cnName="\u9ad8\u8df3")
public class HighJump
extends Module {
    private final FloatValue heightValue = new FloatValue("Height", 2.0f, 1.1f, 10.0f, "m");
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Damage", "AACv3", "DAC", "Mineplex", "MatrixWater"}, "Vanilla");
    private final BoolValue glassValue = new BoolValue("OnlyGlassPane", false);
    public int tick;

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
                break;
            }
            case "matrixwater": {
                if (!HighJump.mc.field_71439_g.func_70090_H()) break;
                if (HighJump.mc.field_71441_e.func_180495_p(new BlockPos(HighJump.mc.field_71439_g.field_70165_t, HighJump.mc.field_71439_g.field_70163_u + 1.0, HighJump.mc.field_71439_g.field_70161_v)).func_177230_c() == Block.func_149729_e((int)9)) {
                    HighJump.mc.field_71439_g.field_70181_x = 0.18;
                    break;
                }
                if (HighJump.mc.field_71441_e.func_180495_p(new BlockPos(HighJump.mc.field_71439_g.field_70165_t, HighJump.mc.field_71439_g.field_70163_u, HighJump.mc.field_71439_g.field_70161_v)).func_177230_c() != Block.func_149729_e((int)9)) break;
                HighJump.mc.field_71439_g.field_70181_x = ((Float)this.heightValue.get()).floatValue();
                HighJump.mc.field_71439_g.field_70122_E = true;
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

