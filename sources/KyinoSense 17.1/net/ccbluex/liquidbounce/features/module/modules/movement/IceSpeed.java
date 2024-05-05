/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.material.Material
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.BlockPos
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

@ModuleInfo(name="IceSpeed", description="Allows you to walk faster on ice.", category=ModuleCategory.MOVEMENT)
public class IceSpeed
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"NCP", "AAC", "Spartan"}, "NCP");

    @Override
    public void onEnable() {
        if (((String)this.modeValue.get()).equalsIgnoreCase("NCP")) {
            Blocks.field_150432_aD.field_149765_K = 0.39f;
            Blocks.field_150403_cj.field_149765_K = 0.39f;
        }
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        String mode = (String)this.modeValue.get();
        if (mode.equalsIgnoreCase("NCP")) {
            Blocks.field_150432_aD.field_149765_K = 0.39f;
            Blocks.field_150403_cj.field_149765_K = 0.39f;
        } else {
            Blocks.field_150432_aD.field_149765_K = 0.98f;
            Blocks.field_150403_cj.field_149765_K = 0.98f;
        }
        if (IceSpeed.mc.field_71439_g.field_70122_E && !IceSpeed.mc.field_71439_g.func_70617_f_() && !IceSpeed.mc.field_71439_g.func_70093_af() && IceSpeed.mc.field_71439_g.func_70051_ag() && (double)IceSpeed.mc.field_71439_g.field_71158_b.field_78900_b > 0.0) {
            Material material;
            if (mode.equalsIgnoreCase("AAC") && ((material = BlockUtils.getMaterial(IceSpeed.mc.field_71439_g.func_180425_c().func_177977_b())) == Material.field_151588_w || material == Material.field_151598_x)) {
                IceSpeed.mc.field_71439_g.field_70159_w *= 1.342;
                IceSpeed.mc.field_71439_g.field_70179_y *= 1.342;
                Blocks.field_150432_aD.field_149765_K = 0.6f;
                Blocks.field_150403_cj.field_149765_K = 0.6f;
            }
            if (mode.equalsIgnoreCase("Spartan") && ((material = BlockUtils.getMaterial(IceSpeed.mc.field_71439_g.func_180425_c().func_177977_b())) == Material.field_151588_w || material == Material.field_151598_x)) {
                Block upBlock = BlockUtils.getBlock(new BlockPos(IceSpeed.mc.field_71439_g.field_70165_t, IceSpeed.mc.field_71439_g.field_70163_u + 2.0, IceSpeed.mc.field_71439_g.field_70161_v));
                if (!(upBlock instanceof BlockAir)) {
                    IceSpeed.mc.field_71439_g.field_70159_w *= 1.342;
                    IceSpeed.mc.field_71439_g.field_70179_y *= 1.342;
                } else {
                    IceSpeed.mc.field_71439_g.field_70159_w *= 1.18;
                    IceSpeed.mc.field_71439_g.field_70179_y *= 1.18;
                }
                Blocks.field_150432_aD.field_149765_K = 0.6f;
                Blocks.field_150403_cj.field_149765_K = 0.6f;
            }
        }
    }

    @Override
    public void onDisable() {
        Blocks.field_150432_aD.field_149765_K = 0.98f;
        Blocks.field_150403_cj.field_149765_K = 0.98f;
        super.onDisable();
    }
}

