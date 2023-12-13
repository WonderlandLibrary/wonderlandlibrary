// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import com.elementars.eclient.event.events.EventResetBlockRemoving;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.elementars.eclient.event.events.EventClickBlock;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.event.events.EventPlayerDamageBlock;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.elementars.eclient.util.LagCompensator;
import com.elementars.eclient.module.player.TpsSync;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ PlayerControllerMP.class })
public class MixinPlayerControllerMP
{
    @Redirect(method = { "onPlayerDamageBlock" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getPlayerRelativeBlockHardness(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F"))
    float getPlayerRelativeBlockHardness(final IBlockState state, final EntityPlayer player, final World worldIn, final BlockPos pos) {
        return state.getPlayerRelativeBlockHardness(player, worldIn, pos) * (TpsSync.isSync() ? (LagCompensator.INSTANCE.getTickRate() / 20.0f) : 1.0f);
    }
    
    @Inject(method = { "onPlayerDamageBlock" }, at = { @At("HEAD") }, cancellable = true)
    public void test(final BlockPos posBlock, final EnumFacing directionFacing, final CallbackInfoReturnable info) {
        final EventPlayerDamageBlock eventPlayerDamageBlock = new EventPlayerDamageBlock(posBlock, directionFacing);
        eventPlayerDamageBlock.call();
        if (eventPlayerDamageBlock.isCancelled()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "clickBlock" }, at = { @At("HEAD") }, cancellable = true)
    public void onClickBlock(final BlockPos posBlock, final EnumFacing directionFacing, final CallbackInfoReturnable info) {
        final EventClickBlock eventClickBlock = new EventClickBlock(posBlock, directionFacing);
        eventClickBlock.call();
        if (eventClickBlock.isCancelled()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "resetBlockRemoving" }, at = { @At("HEAD") }, cancellable = true)
    public void onBlockDestroyed(final CallbackInfo info) {
        final EventResetBlockRemoving eventDestroyBlock = new EventResetBlockRemoving();
        eventDestroyBlock.call();
        if (eventDestroyBlock.isCancelled()) {
            info.cancel();
        }
    }
}
