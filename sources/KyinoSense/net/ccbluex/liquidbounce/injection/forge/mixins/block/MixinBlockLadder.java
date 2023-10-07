/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockLadder
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.properties.PropertyDirection
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.world.IBlockAccess
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.block;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastClimb;
import net.ccbluex.liquidbounce.injection.forge.mixins.block.MixinBlock;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
    public static PropertyDirection field_176382_a;

    @Overwrite
    public void func_180654_a(IBlockAccess worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.func_180495_p(pos);
        if (iblockstate.func_177230_c() instanceof BlockLadder) {
            FastClimb fastClimb = (FastClimb)LiquidBounce.moduleManager.getModule(FastClimb.class);
            float f = fastClimb.getState() && ((String)fastClimb.getModeValue().get()).equalsIgnoreCase("AAC3.0.0") ? 0.99f : 0.125f;
            switch ((EnumFacing)iblockstate.func_177229_b((IProperty)field_176382_a)) {
                case NORTH: {
                    this.func_149676_a(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case SOUTH: {
                    this.func_149676_a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f);
                    break;
                }
                case WEST: {
                    this.func_149676_a(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                default: {
                    this.func_149676_a(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f);
                }
            }
        }
    }
}

