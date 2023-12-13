// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import net.minecraft.block.Block;
import com.elementars.eclient.util.Wrapper;
import net.minecraft.init.Blocks;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.module.misc.Avoid;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import com.elementars.eclient.module.misc.LiquidInteract;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.Xulu;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.BlockLiquid;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(value = { BlockLiquid.class }, priority = 9999)
public class MixinBlockLiquid
{
    @Inject(method = { "modifyAcceleration" }, at = { @At("HEAD") }, cancellable = true)
    public void modifyAcceleration(final World worldIn, final BlockPos pos, final Entity entityIn, final Vec3d motion, final CallbackInfoReturnable<Vec3d> returnable) {
        if (Xulu.MODULE_MANAGER.getModuleByName("Velocity").isToggled()) {
            returnable.setReturnValue(motion);
            returnable.cancel();
        }
    }
    
    @Inject(method = { "canCollideCheck" }, at = { @At("RETURN") }, cancellable = true, require = 1)
    private void IcanCollide(final IBlockState state, final boolean hitIfLiquid, final CallbackInfoReturnable<Boolean> returnable) {
        returnable.setReturnValue(LiquidInteract.INSTANCE.isToggled());
    }
    
    @Inject(method = { "getCollisionBoundingBox" }, at = { @At("HEAD") }, cancellable = true)
    private void getCollision(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos, final CallbackInfoReturnable<AxisAlignedBB> cir) {
        if (Xulu.MODULE_MANAGER.getModule(Avoid.class).isToggled() && Avoid.lava.getValue() && (blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.FLOWING_LAVA) && Wrapper.getMinecraft().world.getBlockState(new BlockPos(Wrapper.getMinecraft().player.getPositionVector()).add(0, 1, 0)).getBlock() != Blocks.LAVA && Wrapper.getMinecraft().world.getBlockState(new BlockPos(Wrapper.getMinecraft().player.getPositionVector()).add(0, 1, 0)).getBlock() != Blocks.FLOWING_LAVA) {
            cir.setReturnValue(Block.FULL_BLOCK_AABB);
            cir.cancel();
        }
    }
}
