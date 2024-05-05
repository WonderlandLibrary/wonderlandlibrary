/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAnvil
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.world.World
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.block;

import net.ccbluex.liquidbounce.injection.forge.mixins.block.MixinBlock;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={BlockAnvil.class})
public abstract class MixinBlockAnvil
extends MixinBlock {
    @Inject(method={"onBlockPlaced"}, cancellable=true, at={@At(value="HEAD")})
    private void injectAnvilCrashFix(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, CallbackInfoReturnable<IBlockState> cir) {
        if ((meta >> 2 & 0xFFFFFFFC) != 0) {
            cir.setReturnValue(super.func_180642_a(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).func_177226_a((IProperty)BlockAnvil.field_176506_a, (Comparable)placer.func_174811_aO().func_176746_e()).func_177226_a((IProperty)BlockAnvil.field_176505_b, (Comparable)Integer.valueOf(2)));
            cir.cancel();
        }
    }
}

