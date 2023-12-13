// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.elementars.eclient.event.events.AddCollisionBoxToListEvent;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockStateContainer.StateImplementation.class })
public class MixinStateImplementation
{
    @Shadow
    @Final
    private Block block;
    
    @Redirect(method = { "addCollisionBoxToList" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;addCollisionBoxToList(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Z)V"))
    public void addCollisionBoxToList(final Block b, final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        final AddCollisionBoxToListEvent event = new AddCollisionBoxToListEvent(b, state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
        event.call();
        if (!event.isCancelled()) {
            this.block.addCollisionBoxToList(state, worldIn, pos, entityBox, (List)collidingBoxes, entityIn, isActualState);
        }
    }
}
