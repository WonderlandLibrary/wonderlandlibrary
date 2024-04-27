/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockLadder
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.properties.PropertyDirection
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.block;

import java.util.Objects;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastClimb;
import net.ccbluex.liquidbounce.injection.forge.mixins.block.MixinBlock;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(value=Side.CLIENT)
@Mixin(value={BlockLadder.class})
public abstract class MixinBlockLadder
extends MixinBlock {
    @Shadow
    @Final
    protected static AxisAlignedBB field_185690_e;
    @Shadow
    @Final
    protected static AxisAlignedBB field_185688_c;
    @Shadow
    @Final
    protected static AxisAlignedBB field_185689_d;
    @Shadow
    @Final
    protected static AxisAlignedBB field_185687_b;
    @Shadow
    @Final
    public static PropertyDirection field_176382_a;

    @Overwrite
    public AxisAlignedBB func_185496_a(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        if (iBlockState.func_177230_c() instanceof BlockLadder) {
            FastClimb fastClimb = (FastClimb)LiquidBounce.moduleManager.getModule(FastClimb.class);
            boolean bl = Objects.requireNonNull(fastClimb).getState() && ((String)fastClimb.getModeValue().get()).equalsIgnoreCase("AAC3.0.0");
            float f = 0.99f;
            if (bl) {
                switch (1.$SwitchMap$net$minecraft$util$EnumFacing[((EnumFacing)iBlockState.func_177229_b((IProperty)field_176382_a)).ordinal()]) {
                    case 1: {
                        return new AxisAlignedBB(0.0, 0.0, (double)0.00999999f, 1.0, 1.0, 1.0);
                    }
                    case 2: {
                        return new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, (double)0.99f);
                    }
                    case 3: {
                        return new AxisAlignedBB((double)0.00999999f, 0.0, 0.0, 1.0, 1.0, 1.0);
                    }
                }
                return new AxisAlignedBB(0.0, 0.0, 0.0, (double)0.99f, 1.0, 1.0);
            }
        }
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[((EnumFacing)iBlockState.func_177229_b((IProperty)field_176382_a)).ordinal()]) {
            case 1: {
                return field_185690_e;
            }
            case 2: {
                return field_185689_d;
            }
            case 3: {
                return field_185688_c;
            }
        }
        return field_185687_b;
    }
}

