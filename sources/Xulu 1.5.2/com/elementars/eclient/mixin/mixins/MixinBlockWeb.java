// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.block.Block;
import com.elementars.eclient.module.combat.SelfWeb;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.module.misc.Avoid;
import com.elementars.eclient.Xulu;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockWeb;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockWeb.class })
public class MixinBlockWeb
{
    @Inject(method = { "getCollisionBoundingBox" }, at = { @At("HEAD") }, cancellable = true)
    private void testReturn(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos, final CallbackInfoReturnable<AxisAlignedBB> cir) {
        if (Xulu.MODULE_MANAGER.getModule(Avoid.class).isToggled() && Avoid.webs.getValue() && !Xulu.MODULE_MANAGER.getModule(SelfWeb.class).isToggled()) {
            cir.setReturnValue(Block.FULL_BLOCK_AABB);
            cir.cancel();
        }
    }
}
