// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.module.render.Xray;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { Block.class }, priority = 9999)
public class MixinBlock
{
    @Inject(method = { "isFullCube" }, at = { @At("HEAD") }, cancellable = true)
    public void isFullCube(final IBlockState state, final CallbackInfoReturnable<Boolean> callback) {
        try {
            if (Xray.INSTANCE != null && Xray.INSTANCE.isToggled()) {
                callback.setReturnValue(Xray.shouldXray(Block.class.cast(this)));
                callback.cancel();
            }
        }
        catch (Exception ex) {}
    }
    
    @Inject(method = { "shouldSideBeRendered" }, at = { @At("HEAD") }, cancellable = true)
    public void shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side, final CallbackInfoReturnable<Boolean> callback) {
        try {
            if (Xray.INSTANCE != null && Xray.INSTANCE.isToggled()) {
                callback.setReturnValue(Xray.shouldXray(Block.class.cast(this)));
            }
        }
        catch (Exception ex) {}
    }
    
    @Inject(method = { "isOpaqueCube" }, at = { @At("HEAD") }, cancellable = true)
    public void isOpaqueCube(final IBlockState state, final CallbackInfoReturnable<Boolean> callback) {
        try {
            if (Xray.INSTANCE != null && Xray.INSTANCE.isToggled()) {
                callback.setReturnValue(Xray.shouldXray(Block.class.cast(this)));
            }
        }
        catch (Exception ex) {}
    }
}
